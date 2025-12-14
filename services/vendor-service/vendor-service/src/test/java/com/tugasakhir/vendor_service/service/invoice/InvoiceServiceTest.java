package com.tugasakhir.vendor_service.service.invoice;

import com.tugasakhir.vendor_service.dto.invoice.InvoiceDTO;
import com.tugasakhir.vendor_service.dto.invoice.InvoiceGenerationRequest;
import com.tugasakhir.vendor_service.dto.invoice.InvoiceMapper;
import com.tugasakhir.vendor_service.kafka.InvoiceProducer;
import com.tugasakhir.vendor_service.model.invoice.Invoice;
import com.tugasakhir.vendor_service.repository.invoice.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private InvoiceRepository repository;

    @Mock
    private InvoiceMapper mapper;

    @Mock
    private InvoiceProducer producer;

    @InjectMocks
    private InvoiceService service;

    private Invoice invoice;
    private InvoiceDTO invoiceDTO;

    @BeforeEach
    void setUp() {
        invoice = new Invoice();
        invoice.setInvoiceId(UUID.randomUUID().toString());
        invoice.setStatusInvoice("DRAFT");
        invoice.setTotalTagihan(new BigDecimal("1000000"));

        invoiceDTO = new InvoiceDTO();
        invoiceDTO.setInvoiceId(invoice.getInvoiceId());
        invoiceDTO.setStatusInvoice(invoice.getStatusInvoice());
    }

    @Test
    void generateDraftInvoice_Success() {
        when(repository.save(any(Invoice.class))).thenReturn(invoice);
        when(mapper.toDto(any(Invoice.class))).thenReturn(invoiceDTO);

        InvoiceGenerationRequest request = new InvoiceGenerationRequest();
        request.setPoId("PO-123");

        InvoiceDTO result = service.generateDraftInvoice(request);

        assertNotNull(result);
        verify(repository, times(1)).save(any(Invoice.class));
    }

    @Test
    void submitInvoice_Success() {
        when(repository.findById(invoice.getInvoiceId())).thenReturn(Optional.of(invoice));
        when(repository.save(any(Invoice.class))).thenReturn(invoice);
        when(mapper.toDto(any(Invoice.class))).thenReturn(invoiceDTO);

        InvoiceDTO result = service.submitInvoice(invoice.getInvoiceId());

        assertNotNull(result);
        verify(producer, times(1)).sendInvoiceSubmittedEvent(any(), any(), any());
    }
}
