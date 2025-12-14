package com.tugasakhir.vendor_service.dto.report;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class VendorReportDTO {
    private String vendorId;
    private String period; // e.g., "2023-Q1"
    private Double onTimeDeliveryRate;
    private Double qualityScore;
    private Double responseTimeAverage; // in hours
    private BigDecimal totalSpend;
    private Integer totalOrders;
}
