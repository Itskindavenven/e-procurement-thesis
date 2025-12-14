package com.tugas_akhir.admin_service.calendar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "calendar_nonactive_days")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NonActiveDay {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "nonactive_id")
    private UUID nonActiveId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(length = 255)
    private String reason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "created_by", length = 50)
    private String createdBy;
}
