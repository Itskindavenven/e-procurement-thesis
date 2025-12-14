package com.tugas_akhir.procurement_service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import com.tugas_akhir.procurement_service.common.enums.ProcurementPriority;
import com.tugas_akhir.procurement_service.common.enums.ProcurementStatus;
import com.tugas_akhir.procurement_service.common.enums.ProcurementType;
import com.tugas_akhir.procurement_service.domain.inventory.entity.InventoryItem;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;

/**
 * Factory class for creating test data objects
 */
public class TestDataFactory {

    public static ProcurementRequest createProcurementRequest() {
        return ProcurementRequest.builder()
                .id(UUID.randomUUID())
                .description("Test Request Entity")
                .status(ProcurementStatus.DRAFT)
                .type(ProcurementType.GOODS)
                .priority(ProcurementPriority.NORMAL)
                .operatorId(UUID.randomUUID())
                .vendorId(UUID.randomUUID())
                .deadline(LocalDateTime.now().plusDays(7))
                .build();
    }

    public static InventoryItem createInventoryItem(String sku, int qty) {
        return InventoryItem.builder()
                .sku(sku)
                .name("Item " + sku)
                .quantityInHand(qty)
                .minimumStockThreshold(10)
                .build();
    }
}
