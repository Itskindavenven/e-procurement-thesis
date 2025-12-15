package com.tugas_akhir.procurement_service.domain.receiving.service;

import com.tugas_akhir.procurement_service.common.enums.POStatus;
import com.tugas_akhir.procurement_service.common.enums.ReceivingStatus;
import com.tugas_akhir.procurement_service.domain.procurementorder.entity.POHeader;
import com.tugas_akhir.procurement_service.domain.procurementorder.repository.POHeaderRepository;
import com.tugas_akhir.procurement_service.domain.receiving.dto.ReceivingDTOs.*;
import com.tugas_akhir.procurement_service.domain.receiving.entity.ReceivingRecord;
import com.tugas_akhir.procurement_service.domain.receiving.repository.ReceivingRecordRepository;
import com.tugas_akhir.procurement_service.exception.ResourceNotFoundException;
import com.tugas_akhir.procurement_service.exception.ValidationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for goods/service receiving workflow
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReceivingService {

    private final ReceivingRecordRepository receivingRecordRepository;
    private final POHeaderRepository poHeaderRepository;
    private final ProcurementEventProducer eventProducer;

    /**
     * List POs awaiting receiving
     */
    @Transactional(readOnly = true)
    public Page<POAwaitingReceivingDTO> listPOsAwaitingReceiving(Pageable pageable) {
        log.info("Fetching POs awaiting receiving");

        Page<POHeader> deliveredPOs = poHeaderRepository.findByStatus(POStatus.DELIVERED, pageable);

        return deliveredPOs.map(this::mapToPOAwaitingDTO);
    }

    /**
     * Get PO details for receiving
     */
    @Transactional(readOnly = true)
    public POForReceivingDTO getPOForReceivingDetails(UUID poId) {
        log.info("Fetching PO for receiving: {}", poId);

        POHeader po = poHeaderRepository.findById(poId)
                .orElseThrow(() -> new ResourceNotFoundException("PO not found: " + poId));

        return mapToPOForReceivingDTO(po);
    }

    /**
     * Internal helper to get PO entity
     */
    private POHeader getPOForReceiving(UUID poId) {
        return poHeaderRepository.findById(poId)
                .orElseThrow(() -> new ResourceNotFoundException("PO not found: " + poId));
    }

    /**
     * Accept full delivery
     */
    @Transactional
    public ReceivingResponseDTO acceptFullDelivery(UUID poId, AcceptFullDeliveryDTO request, UUID operatorId) {
        log.info("Accepting full delivery for PO: {} by operator: {}", poId, operatorId);

        POHeader po = getPOForReceiving(poId);
        validatePOStatus(po, "accept");

        ReceivingRecord record = ReceivingRecord.builder()
                .poHeader(po)
                .condition(request.getCondition())
                .status(ReceivingStatus.ACCEPTED)
                .receivedBy(operatorId)
                .notes(request.getNotes())
                .build();

        receivingRecordRepository.save(record);

        // Update PO status
        po.setStatus(POStatus.COMPLETED);
        po.setUpdatedBy(operatorId);
        poHeaderRepository.save(po);

        // Publish goods received event for inventory update
        eventProducer.publishGoodsReceived(
                new com.tugas_akhir.procurement_service.event.dto.ProcurementEvents.GoodsReceivedEvent(po.getId(),
                        operatorId));

        log.info("Full delivery accepted for PO: {}", poId);

        return createReceivingResponse(record, "Full delivery accepted successfully");
    }

    /**
     * Accept partial delivery
     */
    @Transactional
    public ReceivingResponseDTO acceptPartialDelivery(UUID poId, AcceptPartialDeliveryDTO request, UUID operatorId) {
        log.info("Accepting partial delivery for PO: {} by operator: {}", poId, operatorId);

        POHeader po = getPOForReceiving(poId);
        validatePOStatus(po, "accept partial");

        ReceivingRecord record = ReceivingRecord.builder()
                .poHeader(po)
                .receivedQuantity(request.getReceivedQuantity())
                .condition(request.getCondition())
                .status(ReceivingStatus.PARTIALLY_ACCEPTED)
                .receivedBy(operatorId)
                .notes(request.getNotes())
                .build();

        receivingRecordRepository.save(record);

        // PO remains IN_DELIVERY for remaining items
        // Could update to COMPLETED if all quantities received across multiple partial
        // accepts

        // TODO: Publish partial goods received event
        // eventProducer.publishPartialGoodsReceived(createPartialGoodsReceivedEvent(po,
        // record));

        log.info("Partial delivery accepted for PO: {}", poId);

        return createReceivingResponse(record, "Partial delivery accepted successfully");
    }

    /**
     * Reject delivery
     */
    @Transactional
    public ReceivingResponseDTO rejectDelivery(UUID poId, RejectDeliveryDTO request, UUID operatorId) {
        log.info("Rejecting delivery for PO: {} by operator: {}", poId, operatorId);

        POHeader po = getPOForReceiving(poId);
        validatePOStatus(po, "reject");

        ReceivingRecord record = ReceivingRecord.builder()
                .poHeader(po)
                .status(ReceivingStatus.REJECTED)
                .receivedBy(operatorId)
                .rejectionReason(request.getRejectionReason())
                .notes(request.getNotes())
                .build();

        receivingRecordRepository.save(record);

        // PO could be cancelled or vendor notified to redeliver
        po.setStatus(POStatus.CANCELLED);
        po.setUpdatedBy(operatorId);
        poHeaderRepository.save(po);

        // TODO: Publish delivery rejected event
        // eventProducer.publishDeliveryRejected(createDeliveryRejectedEvent(po,
        // record));

        log.info("Delivery rejected for PO: {}", poId);

        return createReceivingResponse(record, "Delivery rejected");
    }

    /**
     * Return goods
     */
    @Transactional
    public ReceivingResponseDTO returnGoods(UUID poId, ReturnGoodsDTO request, UUID operatorId) {
        log.info("Returning goods for PO: {} by operator: {}", poId, operatorId);

        POHeader po = getPOForReceiving(poId);

        ReceivingRecord record = ReceivingRecord.builder()
                .poHeader(po)
                .receivedQuantity(request.getReturnQuantity())
                .condition(request.getCondition())
                .status(ReceivingStatus.RETURNED)
                .receivedBy(operatorId)
                .rejectionReason(request.getReturnReason())
                .notes(request.getNotes())
                .build();

        receivingRecordRepository.save(record);

        // TODO: Publish goods returned event
        // eventProducer.publishGoodsReturned(createGoodsReturnedEvent(po, record));

        log.info("Goods returned for PO: {}", poId);

        return createReceivingResponse(record, "Goods return recorded");
    }

    // Helper methods

    private void validatePOStatus(POHeader po, String action) {
        if (po.getStatus() != POStatus.DELIVERED && po.getStatus() != POStatus.IN_DELIVERY) {
            throw new ValidationException(
                    String.format("Cannot %s: PO status must be DELIVERED or IN_DELIVERY", action));
        }
    }

    private ReceivingResponseDTO createReceivingResponse(ReceivingRecord record, String message) {
        return ReceivingResponseDTO.builder()
                .receivingId(record.getId())
                .poId(record.getPoHeader().getId())
                .status(record.getStatus().name())
                .receivedAt(record.getReceivedAt())
                .message(message)
                .build();
    }

    private POAwaitingReceivingDTO mapToPOAwaitingDTO(POHeader po) {
        // Calculate days waiting
        LocalDateTime deliveredAt = po.getUpdatedAt(); // Assuming updated when status changed to DELIVERED
        long daysWaiting = java.time.Duration.between(deliveredAt, LocalDateTime.now()).toDays();

        return POAwaitingReceivingDTO.builder()
                .poId(po.getId())
                .poNumber(po.getPoNumber())
                .prId(po.getProcurementRequest().getId())
                .prDescription(po.getProcurementRequest().getDescription())
                .vendorName("Vendor Name") // TODO: Fetch from Vendor Service
                .deliveredAt(deliveredAt)
                .daysWaiting((int) daysWaiting)
                .build();
    }

    private POForReceivingDTO mapToPOForReceivingDTO(POHeader po) {
        return POForReceivingDTO.builder()
                .poId(po.getId())
                .poNumber(po.getPoNumber())
                .vendorId(po.getVendorId())
                .vendorName("Vendor Name") // TODO: Fetch from Vendor Service
                .status(po.getStatus().name())
                .deliveredAt(po.getUpdatedAt())
                .build();
    }
}
