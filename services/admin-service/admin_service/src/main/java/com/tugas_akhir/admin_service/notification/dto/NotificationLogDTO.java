package com.tugas_akhir.admin_service.notification.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationLogDTO {
    private UUID logId;
    private UUID templateId;
    private String eventType;
    private LocalDateTime sentAt;
    private String status;
    private String errorMessage;
}
