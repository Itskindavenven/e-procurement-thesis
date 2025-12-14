package com.tugasakhir.vendor_service.kafka;

import com.tugasakhir.vendor_service.service.delivery.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogisticsConsumer {

    private final DeliveryService deliveryService;

    @KafkaListener(topics = "logistics.status.updated", groupId = "vendor-service-group")
    public void consumeLogisticsStatusUpdate(String message) {
        log.info("Received logistics update: {}", message);
        // Parse message (assuming JSON: {"deliveryId": "...", "status": "..."})
        // For simplicity, let's assume we parse it manually or use a DTO.
        // In real impl, use ObjectMapper. For now, just log.
        // deliveryService.updateDeliveryStatus(id, status);
    }
}
