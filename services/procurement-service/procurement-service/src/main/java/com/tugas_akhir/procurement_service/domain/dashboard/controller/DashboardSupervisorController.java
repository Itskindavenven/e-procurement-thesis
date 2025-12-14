package com.tugas_akhir.procurement_service.domain.dashboard.controller;

import com.tugas_akhir.procurement_service.domain.dashboard.service.DashboardSupervisorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard/supervisor")
@RequiredArgsConstructor
public class DashboardSupervisorController {

    DashboardSupervisorService service;

    @GetMapping("/{supervisorId}")
    public ResponseEntity<Map<String, Object>> getDashboard(
            @PathVariable java.util.UUID supervisorId) {
        return ResponseEntity.ok(service.getDashboardStats(supervisorId));
    }
}

