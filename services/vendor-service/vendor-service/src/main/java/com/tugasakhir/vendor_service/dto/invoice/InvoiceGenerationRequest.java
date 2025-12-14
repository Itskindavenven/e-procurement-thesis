package com.tugasakhir.vendor_service.dto.invoice;

import lombok.Data;

@Data
public class InvoiceGenerationRequest {
    private String poId;
    private String paymentTermId; // Optional, if generating based on specific term
}
