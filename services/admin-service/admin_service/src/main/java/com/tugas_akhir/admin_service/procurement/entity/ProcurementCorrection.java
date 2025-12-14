package com.tugas_akhir.admin_service.procurement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "procurement_corrections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcurementCorrection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "correction_id")
    private UUID correctionId;

    @Column(name = "procurement_id", nullable = false)
    private UUID procurementId;

    @Column(name = "correction_type", nullable = false, length = 50)
    private String correctionType; // METADATA, STATUS, ETC

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "created_by", length = 50)
    private String createdBy;
}
