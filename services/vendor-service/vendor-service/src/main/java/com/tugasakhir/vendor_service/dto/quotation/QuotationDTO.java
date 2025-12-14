package com.tugasakhir.vendor_service.dto.quotation;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class QuotationDTO {
    private String quoteId;
    private String rfqId;
    private String vendorId;
    private BigDecimal totalHarga;
    private String rincianHarga;
    private Integer leadTime;
    private String catatanVendor;
    private String statusQuote;
    private LocalDateTime timestamp;
    private Integer version;
}
