package com.tugas_akhir.procurement_service.domain.procurementorder.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.tugas_akhir.procurement_service.common.enums.POItemStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "po_items")
public class POItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "po_id", nullable = false)
    private POHeader poHeader;

    @Column(name = "item_name")
    private String itemName;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private POItemStatus status;
}

