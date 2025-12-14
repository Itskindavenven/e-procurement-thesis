package com.tugas_akhir.procurement_service.integration.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client for Notification Service
 * Phase 1: Send notification for PR events
 */
@FeignClient(name = "notification-service", url = "${integration.notification-service.url:http://localhost:8084}")
public interface NotificationServiceClient {

    /**
     * Send notification
     */
    @PostMapping("/api/notifications/send")
    void sendNotification(@RequestBody NotificationRequest request);

    /**
     * DTO for notification request
     */
    record NotificationRequest(
            String recipientId,
            String recipientType, // SUPERVISOR, OPERATOR, ADMIN
            String notificationType, // PR_SUBMITTED, PR_APPROVED, etc.
            String title,
            String message,
            String entityType,
            String entityId) {
    }
}
