package com.tugas_akhir.procurement_service.domain.receiving.service;

import com.tugas_akhir.procurement_service.common.enums.POStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.tugas_akhir.procurement_service.client.InventoryServiceClient;
import com.tugas_akhir.procurement_service.domain.procurementorder.dto.POHeaderDTO;
import com.tugas_akhir.procurement_service.domain.procurementorder.entity.POHeader;
import com.tugas_akhir.procurement_service.domain.procurementorder.repository.POHeaderRepository;
import com.tugas_akhir.procurement_service.event.producer.ProcurementEventProducer;
import com.tugas_akhir.procurement_service.mapper.ProcurementMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoodsReceivingService {

    private final POHeaderRepository repository;
    private final ProcurementMapper mapper;
    private final InventoryServiceClient inventoryClient;
    private final ProcurementEventProducer eventProducer;

    @Transactional
    public POHeaderDTO acceptGoods(UUID poId) {
        POHeader entity = repository.findById(poId)
                .orElseThrow(() -> new RuntimeException("PO not found"));

        entity.setStatus(POStatus.ACCEPTED);
        POHeader saved = repository.save(entity);

        // Notify Inventory
        // inventoryClient.updateStock(...);
        // eventProducer.sendGoodsReceivedEvent(...);

        return mapper.toDTO(saved);
    }

    // Add logic for reject/return
}

