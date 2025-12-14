package com.tugasakhir.vendor_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RFQProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendRFQRespondedEvent(String rfqId, String vendorId) {
        String message = String.format("{\"event\": \"vendor.rfq.responded\", \"rfqId\": \"%s\", \"vendorId\": \"%s\"}",
                rfqId, vendorId);
        kafkaTemplate.send("vendor.rfq.responded", rfqId, message);
        log.info("Sent RFQ responded event: {}", message);
    }

    public void sendRFQRevisedEvent(String rfqId, String vendorId, int version) {
        String message = String.format(
                "{\"event\": \"vendor.rfq.revised\", \"rfqId\": \"%s\", \"vendorId\": \"%s\", \"version\": %d}", rfqId,
                vendorId, version);
        kafkaTemplate.send("vendor.rfq.revised", rfqId, message);
        log.info("Sent RFQ revised event: {}", message);
    }

    public void sendRFQDeclinedEvent(String rfqId, String vendorId, String reason) {
        String message = String.format(
                "{\"event\": \"vendor.rfq.declined\", \"rfqId\": \"%s\", \"vendorId\": \"%s\", \"reason\": \"%s\"}",
                rfqId, vendorId, reason);
        kafkaTemplate.send("vendor.rfq.declined", rfqId, message);
        log.info("Sent RFQ declined event: {}", message);
    }
}
