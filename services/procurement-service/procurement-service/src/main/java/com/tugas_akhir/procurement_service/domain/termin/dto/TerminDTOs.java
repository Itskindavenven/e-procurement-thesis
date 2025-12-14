package com.tugas_akhir.procurement_service.domain.termin.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTOs for Termin management
 */
public class TerminDTOs {

    /**
     * Response DTO for termin details
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TerminDetailsResponseDTO {
        private UUID id;
        private UUID procurementRequestId;
        private String prDescription;
        private Integer terminNumber;
        private String phaseName;
        private BigDecimal value;
        private String deliverables;
        private String status;
        private LocalDateTime vendorSubmittedAt;
        private LocalDateTime operatorReviewedAt;
        private LocalDateTime supervisorDecidedAt;
        private String clarificationNotes;
        private String revisionNotes;
        private String operatorReviewNotes;
        private String supervisorNotes;
        private UUID reviewedBy;
        private UUID approvedBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    /**
     * Summary DTO for termin list
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TerminSummaryDTO {
        private UUID id;
        private UUID procurementRequestId;
        private String prDescription;
        private Integer terminNumber;
        private String phaseName;
        private BigDecimal value;
        private String status;
        private LocalDateTime vendorSubmittedAt;
        private int daysWaiting;
    }

    /**
     * DTO for acceptingtermin
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AcceptTerminDTO {
        @NotBlank(message = "Review notes are required")
        private String reviewNotes;
    }

    /**
     * DTO for requesting clarification
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestClarificationDTO {
        @NotBlank(message = "Clarification notes are required")
        private String clarificationNotes;
    }

    /**
     * DTO for requesting revision
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestRevisionDTO {
        @NotBlank(message = "Revision notes are required")
        private String revisionNotes;
    }

    /**
     * Common response for termin actions
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TerminActionResponseDTO {
        private UUID terminId;
        private String newStatus;
        private LocalDateTime actionedAt;
        private String message;
    }
}
