package com.tugas_akhir.procurement_service.domain.audit.controller;

import com.tugas_akhir.procurement_service.common.util.SecurityUtils;
import com.tugas_akhir.procurement_service.domain.audit.dto.ApprovalDTOs.*;
import com.tugas_akhir.procurement_service.domain.audit.service.ApprovalService;
import com.tugas_akhir.procurement_service.domain.procurementrequest.dto.ProcurementRequestDTOs.ProcurementRequestResponseDTO;
import com.tugas_akhir.procurement_service.domain.procurementrequest.service.ProcurementRequestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller for Supervisor approval operations
 */
@Slf4j
@RestController
@RequestMapping("/api/supervisor/approvals")
@RequiredArgsConstructor
@Tag(name = "Supervisor - Approvals", description = "APIs for Supervisor to review and approve procurement requests")
public class SupervisorApprovalController {

    private final ApprovalService approvalService;
    private final ProcurementRequestService procurementRequestService;

    /**
     * Get list of pending approvals
     */
    @GetMapping("/pending")
    @Operation(summary = "Get pending approvals", description = "List all procurement requests awaiting supervisor approval")
    public ResponseEntity<Page<PendingApprovalDTO>> getPendingApprovals(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {

        log.info("Fetching pending approvals");

        Page<PendingApprovalDTO> response = approvalService.getPendingApprovals(pageable);

        return ResponseEntity.ok(response);
    }

    /**
     * Get PR details for approval
     */
    @GetMapping("/pr/{prId}")
    @Operation(summary = "Get PR for approval", description = "Get full procurement request details for review")
    public ResponseEntity<ProcurementRequestResponseDTO> getPRForApproval(
            @PathVariable UUID prId) {

        log.info("Fetching PR for approval: {}", prId);

        ProcurementRequestResponseDTO response = procurementRequestService.getProcurementRequestById(prId);

        return ResponseEntity.ok(response);
    }

    /**
     * Approve procurement request
     */
    @PutMapping("/pr/{prId}/approve")
    @Operation(summary = "Approve PR", description = "Approve a procurement request - triggers PO creation")
    public ResponseEntity<ApprovalResponseDTO> approvePR(
            @PathVariable UUID prId,
            @Valid @RequestBody ApprovalRequestDTO request,
            @AuthenticationPrincipal Jwt jwt,
            @RequestHeader(value = "X-User-Id", required = false) UUID headerUserId) {

        UUID supervisorId = SecurityUtils.getUserIdFromJwt(jwt);
        if (headerUserId != null) {
            SecurityUtils.validateUserIdMatch(headerUserId, jwt);
        }

        log.info("Approving PR: {} by supervisor: {}", prId, supervisorId);

        ApprovalResponseDTO response = approvalService.approveProcurementRequest(prId, request, supervisorId);

        return ResponseEntity.ok(response);
    }

    /**
     * Reject procurement request
     */
    @PutMapping("/pr/{prId}/reject")
    @Operation(summary = "Reject PR", description = "Reject a procurement request with reason")
    public ResponseEntity<ApprovalResponseDTO> rejectPR(
            @PathVariable UUID prId,
            @Valid @RequestBody ApprovalRequestDTO request,
            @AuthenticationPrincipal Jwt jwt,
            @RequestHeader(value = "X-User-Id", required = false) UUID headerUserId) {

        UUID supervisorId = SecurityUtils.getUserIdFromJwt(jwt);
        if (headerUserId != null) {
            SecurityUtils.validateUserIdMatch(headerUserId, jwt);
        }

        log.info("Rejecting PR: {} by supervisor: {}", prId, supervisorId);

        ApprovalResponseDTO response = approvalService.rejectProcurementRequest(prId, request, supervisorId);

        return ResponseEntity.ok(response);
    }

    /**
     * Return procurement request for revision
     */
    @PutMapping("/pr/{prId}/return")
    @Operation(summary = "Return PR for revision", description = "Return PR to operator for corrections/modifications")
    public ResponseEntity<ApprovalResponseDTO> returnPR(
            @PathVariable UUID prId,
            @Valid @RequestBody ApprovalRequestDTO request,
            @AuthenticationPrincipal Jwt jwt,
            @RequestHeader(value = "X-User-Id", required = false) UUID headerUserId) {

        UUID supervisorId = SecurityUtils.getUserIdFromJwt(jwt);
        if (headerUserId != null) {
            SecurityUtils.validateUserIdMatch(headerUserId, jwt);
        }

        log.info("Returning PR: {} by supervisor: {}", prId, supervisorId);

        ApprovalResponseDTO response = approvalService.returnProcurementRequest(prId, request, supervisorId);

        return ResponseEntity.ok(response);
    }

    /**
     * Add feedback/comment without changing status
     */
    @PutMapping("/pr/{prId}/feedback")
    @Operation(summary = "Add feedback", description = "Add comments/feedback to PR without changing its status")
    public ResponseEntity<ApprovalResponseDTO> addFeedback(
            @PathVariable UUID prId,
            @Valid @RequestBody ApprovalRequestDTO request,
            @AuthenticationPrincipal Jwt jwt,
            @RequestHeader(value = "X-User-Id", required = false) UUID headerUserId) {

        UUID supervisorId = SecurityUtils.getUserIdFromJwt(jwt);
        if (headerUserId != null) {
            SecurityUtils.validateUserIdMatch(headerUserId, jwt);
        }

        log.info("Adding feedback to PR: {} by supervisor: {}", prId, supervisorId);

        ApprovalResponseDTO response = approvalService.addFeedback(prId, request, supervisorId);

        return ResponseEntity.ok(response);
    }
}
