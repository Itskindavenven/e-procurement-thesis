package com.tugas_akhir.procurement_service.domain.reporting.controller;

import com.tugas_akhir.procurement_service.domain.reporting.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

        private final ReportingService service;

        @GetMapping("/export")
        public ResponseEntity<byte[]> exportReport(
                        @RequestParam String type) {
                byte[] content = service.generateReport(type);
                return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION,
                                                "attachment; filename=report-" + type + ".csv")
                                .body(content);
        }
}
