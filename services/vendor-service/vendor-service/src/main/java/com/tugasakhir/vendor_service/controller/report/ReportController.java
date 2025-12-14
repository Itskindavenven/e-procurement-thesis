package com.tugasakhir.vendor_service.controller.report;

import com.tugasakhir.vendor_service.dto.report.DashboardDTO;
import com.tugasakhir.vendor_service.dto.report.VendorReportDTO;
import com.tugasakhir.vendor_service.service.report.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor/report")
@RequiredArgsConstructor
@Tag(name = "Vendor Reports", description = "View dashboards and reports")
public class ReportController {

    private final ReportService service;

    @GetMapping("/dashboard")
    @Operation(summary = "Get vendor dashboard")
    public ResponseEntity<DashboardDTO> getDashboard(@RequestParam String vendorId) {
        return ResponseEntity.ok(service.getDashboard(vendorId));
    }

    @GetMapping("/performance")
    @Operation(summary = "Get performance report")
    public ResponseEntity<VendorReportDTO> getPerformanceReport(@RequestParam String vendorId,
            @RequestParam(defaultValue = "Current") String period) {
        return ResponseEntity.ok(service.generateReport(vendorId, period));
    }

    @GetMapping("/export")
    @Operation(summary = "Export report")
    public ResponseEntity<byte[]> exportReport(@RequestParam String vendorId,
            @RequestParam(defaultValue = "PDF") String format) {
        byte[] content = service.exportReport(vendorId, format);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report." + format.toLowerCase())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(content);
    }
}
