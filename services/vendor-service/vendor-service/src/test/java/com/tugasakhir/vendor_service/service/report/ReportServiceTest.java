package com.tugasakhir.vendor_service.service.report;

import com.tugasakhir.vendor_service.dto.report.DashboardDTO;
import com.tugasakhir.vendor_service.dto.report.VendorReportDTO;
import com.tugasakhir.vendor_service.repository.delivery.DeliveryRepository;
import com.tugasakhir.vendor_service.repository.invoice.InvoiceRepository;
import com.tugasakhir.vendor_service.repository.rfq.RFQRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private RFQRepository rfqRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private ReportService service;

    @Test
    void getDashboard_Success() {
        String vendorId = "vendor-1";
        DashboardDTO result = service.getDashboard(vendorId);

        assertNotNull(result);
        assertEquals(vendorId, result.getVendorId());
        assertNotNull(result.getPerformanceMetrics());
    }

    @Test
    void generateReport_Success() {
        String vendorId = "vendor-1";
        VendorReportDTO result = service.generateReport(vendorId, "Current");

        assertNotNull(result);
        assertEquals(vendorId, result.getVendorId());
        assertEquals("Current", result.getPeriod());
    }
}
