package com.tugasakhir.vendor_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tugasakhir.vendor_service.dto.kafka.AdminEventDto;
import com.tugasakhir.vendor_service.service.vendor.VendorAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AdminEventConsumer {

    private final VendorAccountService vendorAccountService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "admin.vendor.events", groupId = "vendor-service-admin-group")
    public void listen(
            @org.springframework.messaging.handler.annotation.Header(org.springframework.kafka.support.KafkaHeaders.RECEIVED_KEY) String key,
            @org.springframework.messaging.handler.annotation.Payload String message) {
        try {
            log.info("Received admin event: key={}, value={}", key, message);

            String vendorId = message;
            String status = null;

            if (key.contains("vendor.approved")) {
                status = "ACTIVE";
            } else if (key.contains("vendor.rejected")) {
                status = "REJECTED";
            } else if (key.contains("vendor.revision.requested")) {
                status = "REVISION_REQUESTED";
            }

            if (status != null) {
                vendorAccountService.updateVendorStatus(vendorId, status);
            }
        } catch (Exception e) {
            log.error("Error processing admin event: key={}, value={}", key, message, e);
        }
    }
}
