package com.tugas_akhir.admin_service.lokasi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "operator_locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatorLocation {

    @Id
    @Column(name = "operator_id")
    private UUID operatorId; // Same as Employee ID

    @Column(name = "location_id", nullable = false)
    private UUID locationId;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt = LocalDateTime.now();

    @Column(name = "assigned_by", length = 50)
    private String assignedBy;
}
