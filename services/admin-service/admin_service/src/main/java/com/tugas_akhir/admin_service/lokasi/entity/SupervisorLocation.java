package com.tugas_akhir.admin_service.lokasi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "supervisor_locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(SupervisorLocationId.class)
public class SupervisorLocation {

    @Id
    @Column(name = "supervisor_id")
    private UUID supervisorId;

    @Id
    @Column(name = "location_id")
    private UUID locationId;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt = LocalDateTime.now();

    @Column(name = "assigned_by", length = 50)
    private String assignedBy;
}
