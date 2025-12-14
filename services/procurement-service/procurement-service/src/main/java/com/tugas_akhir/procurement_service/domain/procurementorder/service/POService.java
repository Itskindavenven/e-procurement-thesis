package com.tugas_akhir.procurement_service.domain.procurementorder.service;

import com.tugas_akhir.procurement_service.common.enums.POStatus;
import com.tugas_akhir.procurement_service.common.enums.ProcurementStatus;
import com.tugas_akhir.procurement_service.domain.procurementorder.entity.POHeader;
import com.tugas_akhir.procurement_service.domain.procurementorder.entity.POItem;
import com.tugas_akhir.procurement_service.domain.procurementorder.repository.POHeaderRepository;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;
import com.tugas_akhir.procurement_service.domain.procurementrequest.repository.ProcurementRequestRepository;
import com.tugas_akhir.procurement_service.event.producer.ProcurementEventProducer;
import com.tugas_akhir.procurement_service.exception.ResourceNotFoundException;
import com.tugas_akhir.procurement_service.exception.ValidationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service for Purchase Order management
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class POService {

    private final POHeaderRepository poHeaderRepository;
    private final ProcurementRequestRepository procurementRequestRepository;
    private final ProcurementEventProducer eventProducer;

    /**
     * Auto-create PO from approved PR
     * Called when PR is approved by supervisor
     */
    @Transactional
    public POHeader createPOFromApprovedPR(UUID prId, UUID approvedBy) {
        log.info("Creating PO from approved PR: {}", prId);

        // Get approved PR
        ProcurementRequest pr = procurementRequestRepository.findById(prId)
                .orElseThrow(() -> new ResourceNotFoundException("PR not found: " + prId));

        // Validate PR is approved
        if (pr.getStatus() != ProcurementStatus.APPROVED) {
            throw new ValidationException("Only approved PRs can generate PO");
        }

        // Check if PO already exists
        if (poHeaderRepository.findByProcurementRequestId(prId).isPresent()) {
            throw new ValidationException("PO already exists for this PR");
        }

        // Generate PO number
        String poNumber = generatePONumber();

        // Create PO Header
        POHeader poHeader = POHeader.builder()
                .procurementRequest(pr)
                .poNumber(poNumber)
                .vendorId(pr.getVendorId())
                .status(POStatus.CREATED)
                .createdBy(approvedBy)
                .build();

        // Create PO Items from PR Items
        List<POItem> poItems = new ArrayList<>();
        pr.getItems().forEach(prItem -> {
            POItem poItem = POItem.builder()
                    .poHeader(poHeader)
                    .itemId(prItem.getId())
                    .itemName(prItem.getItemName())
                    .quantity(prItem.getQuantity())
                    .unitPrice(prItem.getUnitPrice())
                    .subtotal(prItem.getSubtotal())
                    .build();
            poItems.add(poItem);
        });

        poHeader.setItems(poItems);

        // Save PO
        POHeader savedPO = poHeaderRepository.save(poHeader);

        // TODO: Publish PO created event
        // eventProducer.publishPOCreated(createPOCreatedEvent(savedPO));

        log.info("PO created: {} for PR: {}", poNumber, prId);

        return savedPO;
    }

    /**
     * Generate unique PO number
     * Format: PO-YYYYMMDD-XXXXX
     */
    private String generatePONumber() {
        String datePrefix = "PO-" + java.time.LocalDate.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));

        // Get count of POs created today
        long countToday = poHeaderRepository.count(); // Simplified - should filter by today

        String sequence = String.format("%05d", countToday + 1);

        return datePrefix + "-" + sequence;
    }

    /**
     * Update PO status to IN_DELIVERY when vendor ships
     */
    @Transactional
    public void updatePOToInDelivery(UUID poId) {
        POHeader po = poHeaderRepository.findById(poId)
                .orElseThrow(() -> new ResourceNotFoundException("PO not found: " + poId));

        po.setStatus(POStatus.IN_DELIVERY);
        po.setUpdatedAt(LocalDateTime.now());
        poHeaderRepository.save(po);

        log.info("PO {} status updated to IN_DELIVERY", po.getPoNumber());
    }

    /**
     * Update PO status to DELIVERED when goods arrive
     */
    @Transactional
    public void updatePOToDelivered(UUID poId) {
        POHeader po = poHeaderRepository.findById(poId)
                .orElseThrow(() -> new ResourceNotFoundException("PO not found: " + poId));

        po.setStatus(POStatus.DELIVERED);
        po.setUpdatedAt(LocalDateTime.now());
        poHeaderRepository.save(po);

        log.info("PO {} status updated to DELIVERED", po.getPoNumber());
    }
}
