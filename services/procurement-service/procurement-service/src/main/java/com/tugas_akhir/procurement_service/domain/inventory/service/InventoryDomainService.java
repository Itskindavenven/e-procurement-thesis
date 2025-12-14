package com.tugas_akhir.procurement_service.domain.inventory.service;

import com.tugas_akhir.procurement_service.domain.inventory.repository.InventoryRepository;
import com.tugas_akhir.procurement_service.domain.inventory.repository.StockAlertRepository;
import com.tugas_akhir.procurement_service.domain.inventory.repository.StockMutationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tugas_akhir.procurement_service.domain.inventory.entity.InventoryItem;
import com.tugas_akhir.procurement_service.domain.inventory.entity.StockAlert;
import com.tugas_akhir.procurement_service.domain.inventory.entity.StockMutation;
import com.tugas_akhir.procurement_service.event.producer.ProcurementEventProducer;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryDomainService {

    private final InventoryRepository inventoryRepository;
    private final StockMutationRepository stockMutationRepository;
    private final StockAlertRepository stockAlertRepository;
    private final ProcurementEventProducer eventProducer; // Reuse producer

    @Transactional
    public void processGoodsReceived(String sku, String itemName, int quantity, String poReference) {
        InventoryItem item = inventoryRepository.findBySku(sku)
                .orElseGet(() -> createNewItem(sku, itemName));

        incrementStock(item, quantity, poReference, StockMutation.MutationType.IN, "Procurement Receiving");
    }

    @Transactional
    public void manualAdjustment(UUID itemId, int adjustmentQty, String reason, String actor) {
        InventoryItem item = inventoryRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // Adjustment can be negative
        StockMutation.MutationType type = adjustmentQty >= 0 ? StockMutation.MutationType.IN
                : StockMutation.MutationType.OUT;
        incrementStock(item, adjustmentQty, "MANUAL-" + actor, StockMutation.MutationType.ADJUSTMENT, reason);
    }

    private InventoryItem createNewItem(String sku, String name) {
        InventoryItem item = InventoryItem.builder()
                .sku(sku)
                .name(name)
                .quantityInHand(0)
                .minimumStockThreshold(10) // Default
                .build();
        return inventoryRepository.save(item);
    }

    private void incrementStock(InventoryItem item, int qty, String ref, StockMutation.MutationType type,
            String reason) {
        item.setQuantityInHand(item.getQuantityInHand() + qty);
        inventoryRepository.save(item);

        StockMutation mutation = StockMutation.builder()
                .inventoryItem(item)
                .quantity(qty)
                .type(type)
                .referenceId(ref)
                .reason(reason)
                .build();
        stockMutationRepository.save(mutation);

        checkLowStock(item);
    }

    private void checkLowStock(InventoryItem item) {
        if (item.getQuantityInHand() < item.getMinimumStockThreshold()) {
            // Trigger Alert
            StockAlert alert = StockAlert.builder()
                    .inventoryItem(item)
                    .message("Low Stock for " + item.getName() + " (SKU: " + item.getSku() + "). Current: "
                            + item.getQuantityInHand())
                    .isResolved(false)
                    .build();
            stockAlertRepository.save(alert);

            // Publish Event
            eventProducer.sendStockAlert(item.getSku(), item.getQuantityInHand());
        }
    }

    public List<InventoryItem> getAllItems() {
        return inventoryRepository.findAll();
    }
}

