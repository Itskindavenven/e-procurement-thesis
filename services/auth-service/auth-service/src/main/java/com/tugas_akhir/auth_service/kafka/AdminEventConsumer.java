package com.tugas_akhir.auth_service.kafka;

import com.tugas_akhir.auth_service.domain.User;
import com.tugas_akhir.auth_service.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class AdminEventConsumer {

    private final AdminService adminService;

    @KafkaListener(topics = "admin.vendor.events", groupId = "auth-service-admin-group")
    public void listen(
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Payload String message) {
        try {
            log.info("Received admin event: key={}, value={}", key, message);

            UUID userId = UUID.fromString(message);
            User.UserStatus status = null;

            if (key.contains("vendor.approved")) {
                status = User.UserStatus.ACTIVE;
            } else if (key.contains("vendor.rejected")) {
                status = User.UserStatus.REJECTED;
            }

            if (status != null) {
                adminService.updateUserStatus(userId, status);
            }
        } catch (Exception e) {
            log.error("Error processing admin event: key={}, value={}", key, message, e);
        }
    }
}
