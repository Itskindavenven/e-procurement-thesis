package com.tugas_akhir.admin_service.audit.service;

import com.tugas_akhir.admin_service.audit.dto.LogSystemDTO;
import com.tugas_akhir.admin_service.audit.entity.LogSystem;
import com.tugas_akhir.admin_service.audit.mapper.LogSystemMapper;
import com.tugas_akhir.admin_service.audit.repository.LogSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final LogSystemRepository logSystemRepository;
    private final LogSystemMapper logSystemMapper;

    @Transactional(readOnly = true)
    public List<LogSystemDTO> getAllLogs() {
        return logSystemRepository.findAll().stream()
                .map(logSystemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LogSystemDTO> getLogsByService(String serviceName) {
        return logSystemRepository.findByServiceName(serviceName).stream()
                .map(logSystemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void logAction(String serviceName, String action, String userId, String ipAddress, String details) {
        LogSystem log = new LogSystem();
        log.setServiceName(serviceName);
        log.setAction(action);
        log.setUserId(userId);
        log.setIpAddress(ipAddress);
        log.setDetails(details);
        log.setCreatedAt(LocalDateTime.now());

        logSystemRepository.save(log);
    }

    // Example Kafka Listener to consume audit events from other services
    @KafkaListener(topics = "audit.logs", groupId = "admin-service-audit-group")
    public void consumeAuditLog(String message) {
        // Parse message and save to DB
        // For simplicity, we just print it here, but in real impl we would parse JSON
        System.out.println("Received audit log: " + message);
        // Implementation to parse and save would go here
    }

    @Transactional(readOnly = true)
    public java.io.ByteArrayInputStream exportLogs() {
        List<LogSystemDTO> logs = getAllLogs();
        String[] headers = { "Log ID", "Service", "Action", "User ID", "IP Address", "Details", "Timestamp" };
        List<String[]> data = logs.stream().map(log -> new String[] {
                log.getLogId() != null ? log.getLogId().toString() : "",
                log.getServiceName(),
                log.getAction(),
                log.getUserId(),
                log.getIpAddress(),
                log.getDetails(),
                log.getCreatedAt() != null ? log.getCreatedAt().toString() : ""
        }).collect(Collectors.toList());

        return com.tugas_akhir.admin_service.common.util.ExportUtils.simpleCsvExport(data, headers);
    }

    @Transactional
    public void backupLogs(String targetPath) {
        // Simulation of backing up logs to a separate server/storage
        // In a real scenario, this might trigger a batch job or file transfer
        logAction("admin-service", "BACKUP_LOG", "SYSTEM", "127.0.0.1", "Logs backed up to: " + targetPath);
    }

    @Transactional(readOnly = true)
    public List<LogSystemDTO> detectAnomalies() {
        // Simple anomaly detection: Find repeated actions or specific flagged actions
        // Placeholder implementation
        return logSystemRepository.findAll().stream()
                .filter(log -> "LOGIN_FAILED".equals(log.getAction()) || "UNAUTHORIZED_ACCESS".equals(log.getAction()))
                .map(logSystemMapper::toDTO)
                .collect(Collectors.toList());
    }
}
