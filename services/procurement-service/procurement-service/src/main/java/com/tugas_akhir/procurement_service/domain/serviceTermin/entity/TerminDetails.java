package com.tugas_akhir.procurement_service.domain.serviceTermin.entity;

import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.tugas_akhir.procurement_service.common.enums.TerminStatus;
import com.tugas_akhir.procurement_service.domain.serviceProgress.entity.ServiceProgress;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "termin_details")
public class TerminDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private ProcurementRequest procurementRequest;

    @Column(name = "stage_name")
    private String stageName;

    private BigDecimal percentage;

    @Column(name = "expected_start")
    private LocalDateTime expectedStart;

    @Column(name = "expected_end")
    private LocalDateTime expectedEnd;

    @Column(columnDefinition = "TEXT")
    private String deliverable;

    @Column(name = "value_amount")
    private BigDecimal valueAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TerminStatus status;

    @OneToMany(mappedBy = "terminDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceProgress> serviceProgresses;
}
