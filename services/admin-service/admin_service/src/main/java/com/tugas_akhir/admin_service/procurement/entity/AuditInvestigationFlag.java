package com.tugas_akhir.admin_service.procurement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_investigation_flags")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditInvestigationFlag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "flag_id")
    private UUID flagId;

    @Column(name = "procurement_id", nullable = false)
    private UUID procurementId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(name = "flagged_by", nullable = false, length = 50)
    private String flaggedBy;

    @Column(name = "flagged_at", nullable = false)
    private LocalDateTime flaggedAt = LocalDateTime.now();

    @Column(nullable = false, length = 20)
    private String status = "OPEN"; // OPEN, RESOLVED
}
