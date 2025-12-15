package com.tugas_akhir.procurement_service.domain.procurementrequest.entity;

import com.tugas_akhir.procurement_service.domain.termin.entity.TerminDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.SQLRestriction;

import com.tugas_akhir.procurement_service.common.enums.ProcurementPriority;
import com.tugas_akhir.procurement_service.common.enums.ProcurementStatus;
import com.tugas_akhir.procurement_service.common.enums.ProcurementType;
import com.tugas_akhir.procurement_service.domain.delivery.entity.DeliveryDetail;
import com.tugas_akhir.procurement_service.domain.additionaldocument.entity.AdditionalDocument;
import com.tugas_akhir.procurement_service.domain.audit.entity.ApprovalHistory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "procurement_requests")
@SQLRestriction("is_deleted = false")
public class ProcurementRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "operator_id", nullable = false)
    private UUID operatorId;

    @Column(name = "vendor_id", nullable = false)
    private UUID vendorId;

    @Column(name = "location_id")
    private UUID locationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ProcurementType type;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    @Builder.Default
    private ProcurementPriority priority = ProcurementPriority.NORMAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private ProcurementStatus status = ProcurementStatus.DRAFT;

    private LocalDateTime deadline;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "procurementRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProcurementItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "procurementRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DeliveryDetail> deliveryDetails = new ArrayList<>();

    @OneToMany(mappedBy = "procurementRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AdditionalDocument> documents = new ArrayList<>();

    // Phase 2 - Service Procurement
    @OneToMany(mappedBy = "procurementRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TerminDetails> terminDetails = new ArrayList<>();

    @OneToMany(mappedBy = "procurementRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ApprovalHistory> approvalHistories = new ArrayList<>();

    // Phase 3 - PO Management
    @OneToMany(mappedBy = "procurementRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<com.tugas_akhir.procurement_service.domain.procurementorder.entity.POHeader> poHeaders = new ArrayList<>();
}
