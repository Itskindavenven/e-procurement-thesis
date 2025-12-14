package com.tugas_akhir.procurement_service.domain.audit.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTOs for audit and activity tracking
 */
public class AuditDTOs {

    /**
     * Audit log entry
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuditLogDTO {
        private UUID id;
        private String entityType; // PR, PO, TERMIN, RECEIVING
        private UUID entityId;
        private String action; // CREATED, UPDATED, SUBMITTED, APPROVED, etc.
        private UUID performedBy;
        private String performedByName;
        private LocalDateTime timestamp;
        private String oldValue;
        private String newValue;
        private String notes;
    }

    /**
     * Activity feed entry
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActivityFeedDTO {
        private UUID id;
        private String activityType;
        private String description;
        private UUID relatedEntityId;
        private String relatedEntityType;
        private UUID performedBy;
        private String performedByName;
        private LocalDateTime timestamp;
        private String icon; // For UI
        private String severity; // INFO, WARNING, ERROR
    }

    /**
     * PR activity summary
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PRActivitySummaryDTO {
        private UUID prId;
        private String description;
        private Integer totalActivities;
        private LocalDateTime lastActivity;
        private String currentStatus;
    }

    /**
     * Notification preference
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationPreferenceDTO {
        private UUID userId;
        private Boolean emailOnSubmit;
        private Boolean emailOnApproval;
        private Boolean emailOnRejection;
        private Boolean emailOnEscalation;
        private Boolean inAppNotifications;
    }
}
