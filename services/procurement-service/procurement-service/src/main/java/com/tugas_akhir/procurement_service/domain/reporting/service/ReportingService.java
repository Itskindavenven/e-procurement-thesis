package com.tugas_akhir.procurement_service.domain.reporting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportingService {

    private final com.tugas_akhir.procurement_service.domain.procurementrequest.repository.ProcurementRequestRepository prRepository;

    public byte[] generateReport(String reportType) {
        // Simple CSV generation logic
        StringBuilder csv = new StringBuilder();
        csv.append("ID,Description,Status,Created At,Total Amount\n");

        // Fetch last 100 PRs for report
        prRepository.findAll().stream().limit(100).forEach(pr -> {
            csv.append(pr.getId()).append(",")
                    .append("\"").append(pr.getDescription()).append("\",")
                    .append(pr.getStatus()).append(",")
                    .append(pr.getCreatedAt()).append("\n");
        });

        return csv.toString().getBytes();
    }
}
