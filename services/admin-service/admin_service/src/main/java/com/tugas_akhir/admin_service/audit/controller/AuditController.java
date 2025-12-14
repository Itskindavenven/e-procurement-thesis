package com.tugas_akhir.admin_service.audit.controller;

import com.tugas_akhir.admin_service.audit.dto.LogSystemDTO;
import com.tugas_akhir.admin_service.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LogSystemDTO>> getAllLogs() {
        return ResponseEntity.ok(auditService.getAllLogs());
    }

    @GetMapping("/service/{serviceName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LogSystemDTO>> getLogsByService(@PathVariable String serviceName) {
        return ResponseEntity.ok(auditService.getLogsByService(serviceName));
    }
}
