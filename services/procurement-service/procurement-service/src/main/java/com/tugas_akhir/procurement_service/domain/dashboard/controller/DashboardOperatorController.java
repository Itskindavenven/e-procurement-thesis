package com.tugas_akhir.procurement_service.domain.dashboard.controller;

import com.tugas_akhir.procurement_service.domain.dashboard.service.DashboardOperatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard/operator")
@RequiredArgsConstructor
public class DashboardOperatorController {

    private final DashboardOperatorService service;

    @org.springframework.web.bind.annotation.GetMapping("/{operatorId}")
    public org.springframework.http.ResponseEntity<java.util.Map<String, Object>> getDashboard(
            @org.springframework.web.bind.annotation.PathVariable java.util.UUID operatorId) {
        return org.springframework.http.ResponseEntity.ok(service.getDashboardStats(operatorId));
    }
}
