package com.tugasakhir.vendor_service.model.invoice;

import com.tugasakhir.vendor_service.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoices")
public class Invoice extends BaseEntity {

    @Id
    private String invoiceId;

    private String poId;
    private String vendorId;
    private String nomorInvoice;
    private java.time.LocalDate invoiceDate;
    private java.time.LocalDate dueDate;
    private BigDecimal subtotal;
    private BigDecimal totalTagihan;
    private BigDecimal ppn;
    private String terminRef;
    private String statusInvoice; // DRAFT, SUBMITTED, PAID, REJECTED
    private String fileUrl;
    private String catatan;
}
