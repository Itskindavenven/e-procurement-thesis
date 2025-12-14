package com.tugasakhir.vendor_service.model.catalog;

import com.tugasakhir.vendor_service.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vendor_catalog_items")
public class VendorCatalogItem extends BaseEntity {

    @Id
    private String catalogItemId;

    private String vendorId;
    private String sku;
    private String namaItem;
    private String spesifikasi;
    private BigDecimal hargaList;
    private BigDecimal ppn;

    @Column(nullable = false)
    private String statusItem; // ACTIVE, INACTIVE

    private String syncStatus; // PENDING, SYNCED, FAILED
    private String masterDataId;

    @OneToMany(mappedBy = "catalogItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VendorCatalogItemVariation> variations = new ArrayList<>();
}
