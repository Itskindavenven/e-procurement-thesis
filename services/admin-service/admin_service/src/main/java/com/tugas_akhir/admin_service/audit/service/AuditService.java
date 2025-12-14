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
}
