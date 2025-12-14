package com.tugasakhir.vendor_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CatalogProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendCatalogUpdatedEvent(String vendorId, String itemId) {
        String message = String.format(
                "{\"event\": \"vendor.catalog.updated\", \"vendorId\": \"%s\", \"itemId\": \"%s\"}", vendorId, itemId);
        kafkaTemplate.send("vendor.catalog.updated", itemId, message);
        log.info("Sent catalog updated event: {}", message);
    }
}
