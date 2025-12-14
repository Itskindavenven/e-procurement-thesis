package com.tugasakhir.vendor_service.dto.invoice;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InvoiceDTO {
    private String invoiceId;
    private String vendorId;
    private String poId;
    private String invoiceNumber;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String statusInvoice; // DRAFT, SUBMITTED, PAID
    private String paymentTermId;
}
