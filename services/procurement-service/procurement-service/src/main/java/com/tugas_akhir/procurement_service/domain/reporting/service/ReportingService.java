package com.tugas_akhir.procurement_service.domain.reporting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportingService {

    public byte[] generateReport(String reportType) {
        // Placeholder: Generate simplified CSV byte array
        String header = "ID,Type,Date\n";
        String data = "123,PO,2025-12-10\n";
        return (header + data).getBytes();
    }
}

