package com.tugasakhir.vendor_service.dto.report;

import lombok.Data;
import java.util.List;

@Data
public class DashboardDTO {
    private String vendorId;
    private Integer activeRFQs;
    private Integer pendingOrders;
    private Integer pendingInvoices;
    private VendorReportDTO performanceMetrics;
    private List<String> recentNotifications;
}
