package com.tugas_akhir.admin_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tugas_akhir.admin_service.dto.kafka.UserEventDto;
import com.tugas_akhir.admin_service.vendor.dto.VendorDTO;
import com.tugas_akhir.admin_service.vendor.entity.VendorRegistration;
import com.tugas_akhir.admin_service.vendor.repository.VendorRepository;
import com.tugas_akhir.admin_service.vendor.service.VendorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserEventConsumer {

    private final VendorService vendorService;
    private final VendorRepository vendorRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "user.events", groupId = "admin-service-group")
    public void listen(String message) {
        try {
            log.info("Received user event: {}", message);
            UserEventDto event = objectMapper.readValue(message, UserEventDto.class);

            if ("USER_CREATED".equals(event.getEventType())) {
                handleUserCreated(event);
            }
        } catch (Exception e) {
            log.error("Error processing user event: {}", message, e);
        }
    }

    private void handleUserCreated(UserEventDto event) {
        boolean isVendor = event.getRoles() != null && event.getRoles().stream()
                .anyMatch(r -> "ROLE_VENDOR".equals(r.getName()));

        if (!isVendor) {
            return;
        }

        UUID vendorId = UUID.fromString(event.getUserId());
        if (vendorRepository.existsByVendorId(vendorId)) {
            log.info("Vendor registration already exists for vendorId: {}", vendorId);
            return;
        }

        VendorDTO dto = new VendorDTO();
        dto.setVendorId(vendorId);
        dto.setVerificationStatus("PENDING");
        // Other fields like documentUrl might come from a separate event or API call
        // For now, we create the placeholder registration

        vendorService.createVendorRegistration(dto);
        log.info("Created vendor registration for vendorId: {}", vendorId);
    }
}
