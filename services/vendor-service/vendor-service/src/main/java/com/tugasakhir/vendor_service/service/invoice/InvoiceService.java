package com.tugasakhir.vendor_service.service.invoice;

import com.tugasakhir.vendor_service.dto.invoice.InvoiceDTO;
import com.tugasakhir.vendor_service.dto.invoice.InvoiceGenerationRequest;
import com.tugasakhir.vendor_service.dto.invoice.InvoiceMapper;
import com.tugasakhir.vendor_service.exception.ResourceNotFoundException;
import com.tugasakhir.vendor_service.kafka.InvoiceProducer;
import com.tugasakhir.vendor_service.model.invoice.Invoice;
import com.tugasakhir.vendor_service.repository.invoice.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceService {

    private final InvoiceRepository repository;
    private final InvoiceMapper mapper;
    private final InvoiceProducer producer;

    @Transactional
    public InvoiceDTO generateDraftInvoice(InvoiceGenerationRequest request) {
        // Logic to fetch PO details and calculate amount would go here.
        // For now, we mock the PO data.
        BigDecimal poAmount = new BigDecimal("1000000"); // Mock amount
        BigDecimal taxRate = new BigDecimal("0.11");

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(UUID.randomUUID().toString());
        invoice.setPoId(request.getPoId());
        invoice.setVendorId("vendor-123"); // Should be fetched from PO
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setDueDate(LocalDate.now().plusDays(30)); // Default 30 days
        invoice.setSubtotal(poAmount);
        invoice.setPpn(poAmount.multiply(taxRate));
        invoice.setTotalTagihan(poAmount.add(invoice.getPpn()));
        invoice.setStatusInvoice("DRAFT");

        return mapper.toDto(repository.save(invoice));
    }

    @Transactional
    public InvoiceDTO submitInvoice(String invoiceId) {
        Invoice invoice = repository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found: " + invoiceId));

        if (!"DRAFT".equals(invoice.getStatusInvoice())) {
            throw new IllegalStateException("Only draft invoices can be submitted");
        }

        invoice.setStatusInvoice("SUBMITTED");
        Invoice saved = repository.save(invoice);

        producer.sendInvoiceSubmittedEvent(saved.getInvoiceId(), saved.getVendorId(),
                saved.getTotalTagihan().toString());

        return mapper.toDto(saved);
    }

    public void uploadInvoiceDocument(String invoiceId, MultipartFile file) {
        log.info("Uploaded document for invoice {}: {}", invoiceId, file.getOriginalFilename());
        // Save file logic
    }
}
