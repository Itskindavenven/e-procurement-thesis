package com.tugas_akhir.procurement_service.domain.audit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import com.tugas_akhir.procurement_service.common.enums.ApprovalDecision;
import com.tugas_akhir.procurement_service.common.enums.ApprovalRole;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "approval_history")
public class ApprovalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private ProcurementRequest procurementRequest;

    @Column(name = "approver_id")
    private UUID approverId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ApprovalRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision")
    private ApprovalDecision decision;

    @Column(columnDefinition = "TEXT")
    private String note;

    private LocalDateTime timestamp;
}
