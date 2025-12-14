package com.tugas_akhir.procurement_service.domain.delivery.entity;

import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "delivery_details")
public class DeliveryDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private ProcurementRequest procurementRequest;

    @Column(name = "delivery_address", columnDefinition = "TEXT")
    private String deliveryAddress;

    @Column(name = "operator_location")
    private String operatorLocation;

    @Column(name = "planned_date")
    private LocalDateTime plannedDate;

    @Column(columnDefinition = "TEXT")
    private String notes;
}

