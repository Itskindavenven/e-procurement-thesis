package com.tugas_akhir.procurement_service.domain.serviceProgress.entity;

import com.tugas_akhir.procurement_service.domain.termin.entity.TerminDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.tugas_akhir.procurement_service.common.enums.ServiceProgressStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service_progress")
public class ServiceProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "termin_id", nullable = false)
    private TerminDetails terminDetails;

    @Column(name = "progress_percentage")
    private BigDecimal progressPercentage;

    @Column(name = "vendor_report_url", columnDefinition = "TEXT")
    private String vendorReportUrl;

    @Column(name = "supporting_docs_url", columnDefinition = "TEXT")
    private String supportingDocsUrl;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ServiceProgressStatus status;
}
