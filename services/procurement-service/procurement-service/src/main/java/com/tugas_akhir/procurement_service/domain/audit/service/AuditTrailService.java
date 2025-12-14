package com.tugas_akhir.procurement_service.domain.audit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditTrailService {

    // In a real microservices architecture, this might send an event to an Audit
    // Service
    public void logActivity(String userId, String action, String description) {
        // Placeholder: Log to console/SLF4J
        System.out.println("AUDIT | User: " + userId + " | Action: " + action + " | " + description);
    }
}

