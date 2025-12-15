package com.tugas_akhir.procurement_service.domain.dashboard.controller;

import com.tugas_akhir.procurement_service.domain.dashboard.dto.DashboardDTOs.*;
import com.tugas_akhir.procurement_service.domain.dashboard.service.DashboardSupervisorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

/**
 * Controller for supervisor dashboard
 */
@Slf4j
@RestController
@RequestMapping("/api/supervisor/dashboard")
@RequiredArgsConstructor
@Tag(name = "Supervisor - Dashboard", description = "Dashboard and analytics APIs for Supervisor")
@PreAuthorize("hasAnyRole('SUPERVISOR', 'ADMIN')")
public class SupervisorDashboardController {

    private final DashboardSupervisorService dashboardService;

    /**
     * Get supervisor dashboard summary
     */
    @GetMapping
    @Operation(summary = "Get dashboard summary", description = "Get comprehensive dashboard summary for supervisor with approval analytics")
    public ResponseEntity<SupervisorDashboardDTO> getDashboard() {

        log.info("Getting supervisor dashboard");

        SupervisorDashboardDTO dashboard = dashboardService.getSupervisorDashboard();

        return ResponseEntity.ok(dashboard);
    }

    /**
     * Request Budget Top-Up
     */
    @PostMapping("/budget/topup")
    @Operation(summary = "Request Budget Top-Up", description = "Request additional budget allocation")
    public ResponseEntity<Void> requestTopUp(
            @RequestBody BudgetTopUpRequestDTO request) {

        log.info("Requesting budget top-up: amount={}, reason={}", request.getAmount(), request.getReason());

        // Delegate to service
        dashboardService.requestTopUp(request);

        return ResponseEntity.ok().build();
    }

    /**
     * Download Report (PDF/Excel)
     */
    @GetMapping("/reports/download")
    @Operation(summary = "Download Report", description = "Generate and download analytics report")
    public ResponseEntity<Resource> downloadReport(
            @RequestParam String reportType,
            @RequestParam String format) {

        log.info("Downloading report: type={}, format={}", reportType, format);

        // Stub implementation for prototype
        String content = "Report " + reportType + " in " + format;
        ByteArrayResource resource = new ByteArrayResource(content.getBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report." + format.toLowerCase())
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
