package com.tugas_akhir.procurement_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Legacy DTO for termin details - for backward compatibility
 * Prefer using TerminDTOs.TerminDetailsResponseDTO in new code
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TerminDetailsDTO {
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
