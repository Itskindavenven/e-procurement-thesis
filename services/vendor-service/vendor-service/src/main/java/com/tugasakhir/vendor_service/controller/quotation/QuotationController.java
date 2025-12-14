package com.tugasakhir.vendor_service.controller.quotation;

import com.tugasakhir.vendor_service.dto.quotation.QuotationDTO;
import com.tugasakhir.vendor_service.service.quotation.QuotationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor/quotation")
@RequiredArgsConstructor
@Tag(name = "Quotation Management", description = "Manage vendor quotations")
public class QuotationController {

    private final QuotationService service;

    @GetMapping("/{rfqId}")
    @Operation(summary = "Get quotation for RFQ by vendor")
    public ResponseEntity<QuotationDTO> getQuotation(@PathVariable String rfqId, @RequestParam String vendorId) {
        return ResponseEntity.ok(service.getQuotationByRfqAndVendor(rfqId, vendorId));
    }

    @PostMapping("/{rfqId}")
    @Operation(summary = "Submit quotation for RFQ")
    public ResponseEntity<QuotationDTO> submitQuotation(@PathVariable String rfqId, @RequestBody QuotationDTO dto) {
        dto.setRfqId(rfqId);
        return ResponseEntity.ok(service.submitQuotation(dto));
    }
}
