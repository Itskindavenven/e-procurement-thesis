package com.tugas_akhir.procurement_service.domain.audit.controller;

import com.tugas_akhir.procurement_service.domain.audit.dto.AuditDTOs.*;
import com.tugas_akhir.procurement_service.domain.audit.service.AuditLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for audit logs and activity tracking
 */
@Slf4j
@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@Tag(name = "Audit & Activity", description = "Audit logging and activity tracking APIs")
public class AuditController {

    private final AuditLogService auditLogService;

    /**
     * Get activity feed for current user
     */
    @GetMapping("/activity-feed")
    @Operation(summary = "Get activity feed", description = "Get recent activity feed for current user")
    public ResponseEntity<Page<ActivityFeedDTO>> getActivityFeed(
            @RequestHeader("X-User-Id") UUID userId,
            @PageableDefault(size = 20, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("Getting activity feed for user: {}", userId);

        Page<ActivityFeedDTO> activities = auditLogService.getActivityFeed(userId, pageable);

        return ResponseEntity.ok(activities);
    }

    /**
     * Get activity history for a PR
     */
    @GetMapping("/pr/{prId}/history")
    @Operation(summary = "Get PR activity history", description = "Get complete activity history for a procurement request")
    public ResponseEntity<List<AuditLogDTO>> getPRHistory(
            @PathVariable UUID prId) {

        log.info("Getting activity history for PR: {}", prId);

        List<AuditLogDTO> history = auditLogService.getPRActivityHistory(prId);

        return ResponseEntity.ok(history);
    }
}
