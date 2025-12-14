package com.tugasakhir.vendor_service.model.catalog;

import com.tugasakhir.vendor_service.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "vendor_catalog_item_variations")
public class VendorCatalogItemVariation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String variationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_item_id", nullable = false)
    private VendorCatalogItem catalogItem;

    @Column(nullable = false)
    private String variationName; // e.g., "Color: Red, Size: L"

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private BigDecimal priceAdjustment; // Difference from base price, or full price? Let's assume full price override
                                        // if not null, or adjustment.
    // Simpler: Specific price for this variation.
    private BigDecimal price;

    private Integer stock;
}
