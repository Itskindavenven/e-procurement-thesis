package com.tugas_akhir.procurement_service.integration.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "${application.config.notification-service-url}")
public interface NotificationServiceClient {

    @PostMapping("/api/v1/notifications/send")
    void sendNotification(@RequestBody Object notificationRequest); // Replace Object with actual DTO
}

