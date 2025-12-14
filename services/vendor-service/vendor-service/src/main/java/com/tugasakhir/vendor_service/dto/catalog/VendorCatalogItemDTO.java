package com.tugasakhir.vendor_service.dto.catalog;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class VendorCatalogItemDTO {
    private String catalogItemId;
    private String vendorId;
    private String sku;
    private String namaItem;
    private String spesifikasi;
    private BigDecimal hargaList;
    private BigDecimal ppn;
    private String statusItem;
    private String syncStatus;
    private String masterDataId;
    private java.util.List<VendorCatalogItemVariationDTO> variations;
}
