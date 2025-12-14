package com.tugas_akhir.procurement_service.domain.procurementrequest.dto;

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
public class ProcurementItemDTO {
    private UUID id;
    private UUID vendorCatalogId;
    private String itemName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal taxPpn;
    private BigDecimal subtotal;
}

