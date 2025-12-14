package com.tugas_akhir.admin_service.audit.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LogSystemDTO {
    private UUID logId;
    private String serviceName;
    private String action;
    private String userId;
    private String ipAddress;
    private String details;
    private LocalDateTime createdAt;
}
