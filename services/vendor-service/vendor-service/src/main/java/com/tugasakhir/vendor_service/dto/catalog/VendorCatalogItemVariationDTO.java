package com.tugasakhir.vendor_service.dto.catalog;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class VendorCatalogItemVariationDTO {
    private String variationId;
    private String variationName;
    private String sku;
    private BigDecimal price;
    private Integer stock;
}
