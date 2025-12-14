package com.tugas_akhir.procurement_service.domain.procurementorder.entity;

import com.tugas_akhir.procurement_service.common.enums.POStatus;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "po_headers")
public class POHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private ProcurementRequest procurementRequest;

    @Column(name = "po_number", unique = true)
    private String poNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private POStatus status;

    @Column(name = "vendor_id")
    private UUID vendorId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @OneToMany(mappedBy = "poHeader", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<POItem> items;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
