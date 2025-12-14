package com.tugasakhir.vendor_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvoiceProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendInvoiceSubmittedEvent(String invoiceId, String vendorId, String amount) {
        String message = String.format(
                "{\"event\": \"vendor.invoice.submitted\", \"invoiceId\": \"%s\", \"vendorId\": \"%s\", \"amount\": \"%s\"}",
                invoiceId, vendorId, amount);
        kafkaTemplate.send("vendor.invoice.submitted", invoiceId, message);
        log.info("Sent invoice submitted event: {}", message);
    }
}
