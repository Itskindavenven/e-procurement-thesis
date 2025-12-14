package com.tugasakhir.vendor_service.model.quotation;

import com.tugasakhir.vendor_service.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quotation_responses")
public class QuotationResponse extends BaseEntity {

    @Id
    private String quoteId;

    private String rfqId;
    private String vendorId;
    private BigDecimal totalHarga;
    private String rincianHarga;
    private Integer leadTime;
    private String catatanVendor;
    private String statusQuote;
    private LocalDateTime timestamp;

    @jakarta.persistence.Version
    private Integer version;
}
