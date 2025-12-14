package com.tugas_akhir.procurement_service.domain.dashboard.controller;

import com.tugas_akhir.procurement_service.domain.dashboard.dto.DashboardDTOs.*;
import com.tugas_akhir.procurement_service.domain.dashboard.service.DashboardSupervisorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for supervisor dashboard
 */
@Slf4j
@RestController
@RequestMapping("/api/supervisor/dashboard")
@RequiredArgsConstructor
@Tag(name = "Supervisor - Dashboard", description = "Dashboard and analytics APIs for Supervisor")
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
}
