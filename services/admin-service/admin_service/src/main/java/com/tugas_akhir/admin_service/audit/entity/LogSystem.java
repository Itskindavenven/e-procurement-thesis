package com.tugas_akhir.admin_service.audit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "log_systems")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "log_id")
    private UUID logId;

    @Column(name = "service_name", nullable = false, length = 50)
    private String serviceName;

    @Column(name = "action", nullable = false, length = 100)
    private String action;

    @Column(name = "user_id", length = 50)
    private String userId; // Can be UUID or username

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
