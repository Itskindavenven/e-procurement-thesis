package com.tugas_akhir.procurement_service.event.consumer;

import com.tugas_akhir.procurement_service.domain.inventory.service.InventoryDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcurementEventConsumer {

    private final InventoryDomainService inventoryService;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.kafka.topic.vendor-termin-status}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeVendorTerminStatus(String message) {
        log.info("Received Vendor Termin Status event: {}", message);
        // Process event logic for termin
    }

    @KafkaListener(topics = "receiving.completed", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeReceivingCompleted(String message) {
        log.info("Received Receiving Completed event: {}", message);
        // Simplified parsing for demo. In real app, use ObjectMapper.
        // Assuming message format: "SKU,ItemName,Qty,PO-REF" for simplicity or just
        // trigger dummy
        try {
            // Expected format: {"sku":"...", "itemName":"...", "quantity":10,
            // "poReference":"..."}
            com.fasterxml.jackson.databind.JsonNode node = objectMapper.readTree(message);
            String sku = node.get("sku").asText();
            String itemName = node.path("itemName").asText("Unknown");
            int quantity = node.get("quantity").asInt();
            String poReference = node.path("poReference").asText("REF-" + System.currentTimeMillis());

            inventoryService.processGoodsReceived(sku, itemName, quantity, poReference);
            log.info("Processed stock update for received goods: {}", sku);
        } catch (Exception e) {
            log.error("Failed to process receiving event", e);
        }
    }
}
