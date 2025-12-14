package com.tugasakhir.vendor_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tugasakhir.vendor_service.dto.kafka.UserEventDto;
import com.tugasakhir.vendor_service.service.vendor.VendorAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserEventConsumer {

    private final VendorAccountService vendorAccountService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "user.events", groupId = "vendor-service-group")
    public void listen(String message) {
        try {
            log.info("Received user event: {}", message);
            UserEventDto event = objectMapper.readValue(message, UserEventDto.class);

            if ("USER_CREATED".equals(event.getEventType()) || "USER_UPDATED".equals(event.getEventType())) {
                vendorAccountService.syncVendorFromUser(event);
            }
        } catch (Exception e) {
            log.error("Error processing user event: {}", message, e);
        }
    }
}
