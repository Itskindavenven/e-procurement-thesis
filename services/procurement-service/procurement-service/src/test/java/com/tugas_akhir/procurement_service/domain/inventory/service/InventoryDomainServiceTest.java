package com.tugas_akhir.procurement_service.domain.inventory.service;

import com.tugas_akhir.procurement_service.domain.inventory.entity.InventoryItem;
import com.tugas_akhir.procurement_service.domain.inventory.entity.StockAlert;
import com.tugas_akhir.procurement_service.domain.inventory.entity.StockMutation;
import com.tugas_akhir.procurement_service.domain.inventory.repository.InventoryRepository;
import com.tugas_akhir.procurement_service.domain.inventory.repository.StockAlertRepository;
import com.tugas_akhir.procurement_service.domain.inventory.repository.StockMutationRepository;
import com.tugas_akhir.procurement_service.domain.inventory.service.InventoryDomainService;
import com.tugas_akhir.procurement_service.event.producer.ProcurementEventProducer;
import com.tugas_akhir.procurement_service.TestDataFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryDomainServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private StockMutationRepository mutationRepository;
    @Mock
    private StockAlertRepository alertRepository;
    @Mock
    private ProcurementEventProducer eventProducer;

    @InjectMocks
    private InventoryDomainService service;

    @Test
    void shouldIncrementStock_andCheckAlerts() {
        // Arrange
        String sku = "SKU-123";
        InventoryItem item = TestDataFactory.createInventoryItem(sku, 5); // Low stock
        when(inventoryRepository.findBySku(sku)).thenReturn(Optional.of(item));

        // Act
        service.processGoodsReceived(sku, "Item Name", 10, "PO-001");

        // Assert
        verify(inventoryRepository).save(item);
        // 5 + 10 = 15, threshold is 10. Should NOT trigger alert
        verify(eventProducer, never()).sendStockAlert(anyString(), anyInt());
        verify(mutationRepository).save(any(StockMutation.class));
    }

    @Test
    void shouldTriggerAlert_whenStockRemainsLow() {
        // Arrange
        String sku = "SKU-LOW";
        InventoryItem item = TestDataFactory.createInventoryItem(sku, 2); // Very low
        when(inventoryRepository.findBySku(sku)).thenReturn(Optional.of(item));

        // Act
        service.processGoodsReceived(sku, "Item Name", 1, "PO-002"); // 2+1=3 < 10

        // Assert
        verify(eventProducer).sendStockAlert(eq(sku), eq(3));
        verify(alertRepository).save(any(StockAlert.class));
    }
}
