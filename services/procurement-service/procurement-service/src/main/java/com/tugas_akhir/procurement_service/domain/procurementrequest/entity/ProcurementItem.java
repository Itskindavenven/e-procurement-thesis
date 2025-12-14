package com.tugas_akhir.procurement_service.domain.procurementrequest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "procurement_items")
public class ProcurementItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private ProcurementRequest procurementRequest;

    @Column(name = "vendor_catalog_id")
    private UUID vendorCatalogId;

    @Column(name = "item_name")
    private String itemName;

    private Integer quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "tax_ppn")
    private BigDecimal taxPpn;

    private BigDecimal subtotal;
}

