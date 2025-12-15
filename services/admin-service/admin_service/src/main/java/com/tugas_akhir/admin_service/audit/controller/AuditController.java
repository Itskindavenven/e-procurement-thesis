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

    @PostMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<org.springframework.core.io.Resource> exportLogs() {
        java.io.ByteArrayInputStream in = auditService.exportLogs();
        org.springframework.core.io.InputStreamResource file = new org.springframework.core.io.InputStreamResource(in);

        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=audit_logs.csv")
                .contentType(org.springframework.http.MediaType.parseMediaType("application/csv"))
                .body(file);
    }

    @PostMapping("/backup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> backupLogs(@RequestBody java.util.Map<String, String> payload) {
        String targetPath = payload.getOrDefault("targetPath", "/default/backup/path");
        auditService.backupLogs(targetPath);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/anomalies")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LogSystemDTO>> detectAnomalies() {
        return ResponseEntity.ok(auditService.detectAnomalies());
    }
}
