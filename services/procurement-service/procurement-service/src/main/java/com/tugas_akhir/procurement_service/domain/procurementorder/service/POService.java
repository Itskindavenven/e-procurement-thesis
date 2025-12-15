package com.tugas_akhir.procurement_service.domain.procurementorder.service;

import com.tugas_akhir.procurement_service.common.enums.POItemStatus;
import com.tugas_akhir.procurement_service.common.enums.POStatus;
import com.tugas_akhir.procurement_service.domain.procurementorder.entity.POHeader;
import com.tugas_akhir.procurement_service.domain.procurementorder.entity.POItem;
import com.tugas_akhir.procurement_service.domain.procurementorder.repository.POHeaderRepository;
import com.tugas_akhir.procurement_service.domain.procurementorder.repository.POItemRepository;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementItem;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;
import com.tugas_akhir.procurement_service.event.dto.ProcurementEvents.POCreatedEvent;
import com.tugas_akhir.procurement_service.event.producer.ProcurementEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class POService {

    private final POHeaderRepository poHeaderRepository;
    private final POItemRepository poItemRepository;
    private final ProcurementEventProducer eventProducer;

    /**
     * Create a Purchase Order from an approved Procurement Request
     */
    @Transactional
    public POHeader createPOFromPR(ProcurementRequest pr) {
        log.info("Creating Purchase Order from PR: {}", pr.getId());

        // Generate PO Number
        String poNumber = "PO-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4);

        POHeader po = POHeader.builder()
                .procurementRequest(pr)
                .poNumber(poNumber)
                .status(POStatus.CREATED)
                .vendorId(pr.getVendorId())
                .createdBy(pr.getUpdatedBy()) // Supervisor who approved
                .build();

        po = poHeaderRepository.save(po);

        // Map PR Items to PO Items
        POHeader finalPo = po;
        List<POItem> poItems = pr.getItems().stream().map(prItem -> mapToPOItem(prItem, finalPo))
                .collect(Collectors.toList());

        poItemRepository.saveAll(poItems);

        log.info("Purchase Order created successfully: {}", po.getId());

        // Publish Event
        eventProducer.publishPOCreated(POCreatedEvent.builder()
                .poId(po.getId())
                .poNumber(po.getPoNumber())
                .vendorId(po.getVendorId())
                .createdAt(LocalDateTime.now())
                .build());

        return po;
    }

    private POItem mapToPOItem(ProcurementItem prItem, POHeader po) {
        return POItem.builder()
                .poHeader(po)
                .itemName(prItem.getItemName())
                .quantity(prItem.getQuantity())
                .status(POItemStatus.PENDING)
                .build();
    }
}
