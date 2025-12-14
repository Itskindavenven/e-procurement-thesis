package com.tugas_akhir.procurement_service.domain.audit.dto;

import com.tugas_akhir.procurement_service.common.enums.ApprovalDecision;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTOs for Approval operations
 */
public class ApprovalDTOs {

    /**
     * Request DTO for making an approval decision
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApprovalRequestDTO {

        @NotNull(message = "Decision is required")
        private ApprovalDecision decision;

        private String note;
    }

    /**
     * Response DTO for approval action
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApprovalResponseDTO {

        private UUID approvalId;
        private UUID procurementRequestId;
        private ApprovalDecision decision;
        private String note;
        private UUID approverId;
        private LocalDateTime approvedAt;
        private String newStatus; // Updated PR status
    }

    /**
     * DTO for listing pending approvals
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PendingApprovalDTO {

        private UUID procurementRequestId;
        private String operatorName;
        private String vendorName;
        private String description;
        private String type;
        private String priority;
        private LocalDateTime submittedAt;
        private LocalDateTime deadline;
        private Integer daysWaiting; // Days since submission
    }
}
