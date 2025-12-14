package com.tugas_akhir.procurement_service.domain.audit.service;

import com.tugas_akhir.procurement_service.domain.audit.dto.AuditDTOs.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service for audit logging and activity tracking
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogService {

    // TODO: Add AuditLogRepository when audit_logs table is created

    /**
     * Log an audit entry
     */
    public void logAudit(String entityType, UUID entityId, String action,
            UUID performedBy, String notes) {
        log.info("AUDIT: {} {} - {} by {} ({})", entityType, entityId, action, performedBy, notes);

        // TODO: Save to database
        AuditLogDTO auditLog = AuditLogDTO.builder()
                .entityType(entityType)
                .entityId(entityId)
                .action(action)
                .performedBy(performedBy)
                .timestamp(LocalDateTime.now())
                .notes(notes)
                .build();

        // For now, just log to console
        // In production, save to audit_logs table
    }

    /**
     * Log PR action
     */
    public void logPRAction(UUID prId, String action, UUID performedBy, String notes) {
        logAudit("PROCUREMENT_REQUEST", prId, action, performedBy, notes);
    }

    /**
     * Log approval action
     */
    public void logApprovalAction(UUID prId, String decision, UUID supervisorId, String notes) {
        logAudit("APPROVAL", prId, decision, supervisorId, notes);
    }

    /**
     * Log termin action
     */
    public void logTerminAction(UUID terminId, String action, UUID operatorId, String notes) {
        logAudit("TERMIN", terminId, action, operatorId, notes);
    }

    /**
     * Log receiving action
     */
    public void logReceivingAction(UUID receivingId, String action, UUID operatorId, String notes) {
        logAudit("RECEIVING", receivingId, action, operatorId, notes);
    }

    /**
     * Get activity feed for user
     */
    public Page<ActivityFeedDTO> getActivityFeed(UUID userId, Pageable pageable) {
        log.info("Getting activity feed for user: {}", userId);

        // TODO: Query from database
        // For now, return empty page
        List<ActivityFeedDTO> activities = new ArrayList<>();

        return new PageImpl<>(activities, pageable, 0);
    }

    /**
     * Get PR activity history
     */
    public List<AuditLogDTO> getPRActivityHistory(UUID prId) {
        log.info("Getting activity history for PR: {}", prId);

        // TODO: Query from audit_logs table where entity_type = 'PR' and entity_id =
        // prId
        return new ArrayList<>();
    }
}
