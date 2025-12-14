package com.tugasakhir.vendor_service.controller.invoice;

import com.tugasakhir.vendor_service.dto.invoice.InvoiceDTO;
import com.tugasakhir.vendor_service.dto.invoice.InvoiceGenerationRequest;
import com.tugasakhir.vendor_service.service.invoice.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/vendor/invoice")
@RequiredArgsConstructor
@Tag(name = "Invoice Management", description = "Manage vendor invoices")
public class InvoiceController {

    private final InvoiceService service;

    @PostMapping("/generate-draft")
    @Operation(summary = "Generate draft invoice from PO")
    public ResponseEntity<InvoiceDTO> generateDraft(@RequestBody InvoiceGenerationRequest request) {
        return ResponseEntity.ok(service.generateDraftInvoice(request));
    }

    @PostMapping("/{id}/submit")
    @Operation(summary = "Submit invoice")
    public ResponseEntity<InvoiceDTO> submitInvoice(@PathVariable String id) {
        return ResponseEntity.ok(service.submitInvoice(id));
    }

    @PostMapping(value = "/{id}/document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload invoice document")
    public ResponseEntity<Void> uploadDocument(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        service.uploadInvoiceDocument(id, file);
        return ResponseEntity.ok().build();
    }
}
