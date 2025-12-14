package com.tugas_akhir.procurement_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client for Inventory Service integration
 */
@FeignClient(name = "inventory-service", url = "${integration.inventory-service.url:http://localhost:8086}")
public interface InventoryServiceClient {

    /**
     * Update inventory stock after goods received
     */
    @PostMapping("/api/inventory/stock/update")
    void updateStock(@RequestBody StockUpdateRequest request);

    /**
     * DTO for stock update request
     */
    record StockUpdateRequest(
            String sku,
            Integer quantity,
            String operation, // ADD, SUBTRACT
            String reason,
            java.util.UUID referenceId) {
    }
}
