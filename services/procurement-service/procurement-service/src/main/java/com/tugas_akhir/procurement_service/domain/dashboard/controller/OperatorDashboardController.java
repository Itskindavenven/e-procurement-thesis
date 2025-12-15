package com.tugas_akhir.procurement_service.domain.dashboard.controller;

import com.tugas_akhir.procurement_service.domain.dashboard.dto.DashboardDTOs.*;
import com.tugas_akhir.procurement_service.domain.dashboard.service.DashboardOperatorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.util.UUID;

/**
 * Controller for operator dashboard
 */
@Slf4j
@RestController
@RequestMapping("/api/operator/dashboard")
@RequiredArgsConstructor
@Tag(name = "Operator - Dashboard", description = "Dashboard and reporting APIs for Operator")
@PreAuthorize("hasRole('OPERATOR')")
public class OperatorDashboardController {

    private final DashboardOperatorService dashboardService;

    /**
     * Get operator dashboard summary
     */
    @GetMapping
    @Operation(summary = "Get dashboard summary", description = "Get comprehensive dashboard summary for operator")
    public ResponseEntity<OperatorDashboardDTO> getDashboard(
            @RequestHeader("X-User-Id") UUID operatorId) {

        log.info("Getting dashboard for operator: {}", operatorId);

        OperatorDashboardDTO dashboard = dashboardService.getOperatorDashboard(operatorId);

        return ResponseEntity.ok(dashboard);
    }

    /**
     * Get PR status breakdown
     */
    @GetMapping("/pr-status")
    @Operation(summary = "Get PR status breakdown", description = "Get count and value breakdown by PR status")
    public ResponseEntity<PRStatusBreakdownDTO> getPRStatusBreakdown(
            @RequestHeader("X-User-Id") UUID operatorId) {

        log.info("Getting PR status breakdown for operator: {}", operatorId);

        PRStatusBreakdownDTO breakdown = dashboardService.getPRStatusBreakdown(operatorId);

        return ResponseEntity.ok(breakdown);
    }

    /**
     * Get monthly statistics
     */
    @GetMapping("/monthly-stats")
    @Operation(summary = "Get monthly statistics", description = "Get PR statistics for a specific month")
    public ResponseEntity<MonthlyPRStatsDTO> getMonthlyStats(
            @RequestHeader("X-User-Id") UUID operatorId,
            @RequestParam int year,
            @RequestParam int month) {

        log.info("Getting monthly stats for operator: {}, year: {}, month: {}", operatorId, year, month);

        MonthlyPRStatsDTO stats = dashboardService.getMonthlyStats(operatorId, year, month);

        return ResponseEntity.ok(stats);
    }

    /**
     * Download Report (PDF/Excel)
     */
    @GetMapping("/reports/download")
    @Operation(summary = "Download Report", description = "Generate and download procurement report")
    public ResponseEntity<Resource> downloadReport(
            @RequestHeader("X-User-Id") UUID operatorId,
            @RequestParam String reportType,
            @RequestParam String format) {

        log.info("Downloading report for operator: {}, type={}, format={}", operatorId, reportType, format);

        // Stub implementation for prototype
        String content = "Operator Report " + reportType + " in " + format;
        ByteArrayResource resource = new ByteArrayResource(content.getBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=operator_report." + format.toLowerCase())
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
