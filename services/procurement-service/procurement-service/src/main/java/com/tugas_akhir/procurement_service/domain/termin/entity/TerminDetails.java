package com.tugas_akhir.procurement_service.domain.termin.entity;

import com.tugas_akhir.procurement_service.common.enums.TerminStatus;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing service termin (milestone/payment term)
 * For service procurement - tracks payment phases and deliverables
 */
@Entity
@Table(name = "termin_details")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
public class TerminDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procurement_request_id", nullable = false)
    private ProcurementRequest procurementRequest;

    @Column(name = "termin_number", nullable = false)
    private Integer terminNumber; // 1, 2, 3, etc.

    @Column(name = "phase_name", nullable = false, length = 200)
    private String phaseName; // e.g., "Initial Design", "Implementation", "Final Delivery"

    @Column(name = "value", nullable = false, precision = 15, scale = 2)
    private BigDecimal value; // Payment amount for this termin

    @Column(name = "deliverables", columnDefinition = "TEXT")
    private String deliverables; // Expected deliverables for this phase

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    @Builder.Default
    private TerminStatus status = TerminStatus.SUBMITTED_BY_VENDOR;

    @Column(name = "vendor_submitted_at")
    private LocalDateTime vendorSubmittedAt;

    @Column(name = "operator_reviewed_at")
    private LocalDateTime operatorReviewedAt;

    @Column(name = "supervisor_decided_at")
    private LocalDateTime supervisorDecidedAt;

    @Column(name = "clarification_notes", columnDefinition = "TEXT")
    private String clarificationNotes; // Notes when requesting clarification

    @Column(name = "revision_notes", columnDefinition = "TEXT")
    private String revisionNotes; // Notes when requesting revision

    @Column(name = "operator_review_notes", columnDefinition = "TEXT")
    private String operatorReviewNotes; // General review notes from operator

    @Column(name = "supervisor_notes", columnDefinition = "TEXT")
    private String supervisorNotes; // Notes from supervisor decision

    @Column(name = "reviewed_by")
    private UUID reviewedBy; // Operator who reviewed

    @Column(name = "approved_by")
    private UUID approvedBy; // Supervisor who approved/rejected

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
