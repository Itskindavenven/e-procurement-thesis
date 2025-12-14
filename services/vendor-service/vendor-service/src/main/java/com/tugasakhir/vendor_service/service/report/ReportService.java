package com.tugasakhir.vendor_service.service.report;

import com.tugasakhir.vendor_service.dto.report.DashboardDTO;
import com.tugasakhir.vendor_service.dto.report.VendorReportDTO;
import com.tugasakhir.vendor_service.repository.delivery.DeliveryRepository;
import com.tugasakhir.vendor_service.repository.invoice.InvoiceRepository;
import com.tugasakhir.vendor_service.repository.rfq.RFQRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final RFQRepository rfqRepository;
    private final DeliveryRepository deliveryRepository;
    private final InvoiceRepository invoiceRepository;

    public DashboardDTO getDashboard(String vendorId) {
        DashboardDTO dashboard = new DashboardDTO();
        dashboard.setVendorId(vendorId);

        // Mock aggregation logic
        dashboard.setActiveRFQs(5); // Should count from rfqRepository where status=OPEN
        dashboard.setPendingOrders(2); // Should count from deliveryRepository where status=PENDING
        dashboard.setPendingInvoices(1); // Should count from invoiceRepository where status=DRAFT

        dashboard.setPerformanceMetrics(generateReport(vendorId, "Current"));
        dashboard.setRecentNotifications(List.of("New RFQ Received", "Invoice Paid"));

        return dashboard;
    }

    public VendorReportDTO generateReport(String vendorId, String period) {
        VendorReportDTO report = new VendorReportDTO();
        report.setVendorId(vendorId);
        report.setPeriod(period);

        // Mock KPI calculation
        report.setOnTimeDeliveryRate(95.0);
        report.setQualityScore(4.8);
        report.setResponseTimeAverage(2.5); // hours
        report.setTotalSpend(new BigDecimal("50000000"));
        report.setTotalOrders(10);

        return report;
    }

    public byte[] exportReport(String vendorId, String format) {
        // Mock export logic
        String content = "Report for Vendor " + vendorId + "\nFormat: " + format;
        return content.getBytes();
    }
}
