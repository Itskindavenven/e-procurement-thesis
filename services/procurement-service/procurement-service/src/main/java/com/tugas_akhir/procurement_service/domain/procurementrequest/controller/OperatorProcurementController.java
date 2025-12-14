package com.tugas_akhir.procurement_service.domain.procurementrequest.controller;

import com.tugas_akhir.procurement_service.common.enums.ProcurementStatus;
import com.tugas_akhir.procurement_service.common.util.SecurityUtils;
import com.tugas_akhir.procurement_service.domain.procurementrequest.dto.ProcurementRequestDTOs.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller for Operator procurement request operations
 */
@Slf4j
@RestController
@RequestMapping("/api/operator/procurement/requests")
@RequiredArgsConstructor
@Tag(name = "Operator - Procurement Requests", description = "APIs for Operator to manage procurement requests")
public class OperatorProcurementController {

    private final ProcurementRequestService procurementRequestService;

    /**
     * Create a new procurement request (draft)
     */
    @PostMapping
    @Operation(summary = "Create new procurement request", description = "Create a new PR as draft for goods or service procurement")
    public ResponseEntity<ProcurementRequestResponseDTO> createProcurementRequest(
            @Valid @RequestBody CreateProcurementRequestDTO request,
            @AuthenticationPrincipal Jwt jwt,
            @RequestHeader(value = "X-User-Id", required = false) UUID headerUserId) {

        UUID operatorId = SecurityUtils.getUserIdFromJwt(jwt);

        // Backward compatibility: validate header matches JWT if provided
        if (headerUserId != null) {
            SecurityUtils.validateUserIdMatch(headerUserId, jwt);
        }

        log.info("Creating procurement request for operator: {}", operatorId);

        ProcurementRequestResponseDTO response = procurementRequestService.createProcurementRequest(request,
                operatorId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update a draft procurement request
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update draft PR", description = "Update a draft or returned procurement request")
    public ResponseEntity<ProcurementRequestResponseDTO> updateProcurementRequest(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProcurementRequestDTO request,
            @AuthenticationPrincipal Jwt jwt,
            @RequestHeader(value = "X-User-Id", required = false) UUID headerUserId) {

        UUID operatorId = SecurityUtils.getUserIdFromJwt(jwt);
        if (headerUserId != null) {
            SecurityUtils.validateUserIdMatch(headerUserId, jwt);
        }

        log.info("Updating procurement request: {} by operator: {}", id, operatorId);

        ProcurementRequestResponseDTO response = procurementRequestService.updateProcurementRequest(id, request,
                operatorId);

        return ResponseEntity.ok(response);
    }

    /**
     * Submit procurement request for approval
     */
    @PutMapping("/{id}/submit")
    @Operation(summary = "Submit PR for approval", description = "Submit a draft or returned PR to Supervisor for approval")
    public ResponseEntity<ProcurementRequestResponseDTO> submitForApproval(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt,
            @RequestHeader(value = "X-User-Id", required = false) UUID headerUserId) {

        UUID operatorId = SecurityUtils.getUserIdFromJwt(jwt);
        if (headerUserId != null) {
            SecurityUtils.validateUserIdMatch(headerUserId, jwt);
        }

        log.info("Submitting procurement request: {} for approval by operator: {}", id, operatorId);

        ProcurementRequestResponseDTO response = procurementRequestService.submitForApproval(id, operatorId);

        return ResponseEntity.ok(response);
    }

    /**
     * Get procurement request by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get PR details", description = "Get full details of a procurement request")
    public ResponseEntity<ProcurementRequestResponseDTO> getProcurementRequest(
            @PathVariable UUID id) {

        log.info("Fetching procurement request: {}", id);

        ProcurementRequestResponseDTO response = procurementRequestService.getProcurementRequestById(id);

        return ResponseEntity.ok(response);
    }

    /**
     * List procurement requests for operator
     */
    @GetMapping
    @Operation(summary = "List PRs", description = "List all procurement requests for the operator with optional status filter")
    public ResponseEntity<Page<ProcurementRequestSummaryDTO>> listProcurementRequests(
            @AuthenticationPrincipal Jwt jwt,
            @RequestHeader(value = "X-User-Id", required = false) UUID headerUserId,
            @RequestParam(required = false) ProcurementStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        UUID operatorId = SecurityUtils.getUserIdFromJwt(jwt);
        if (headerUserId != null) {
            SecurityUtils.validateUserIdMatch(headerUserId, jwt);
        }

        log.info("Listing procurement requests for operator: {}, status: {}", operatorId, status);

        Page<ProcurementRequestSummaryDTO> response = procurementRequestService.listProcurementRequests(operatorId,
                status, pageable);

        return ResponseEntity.ok(response);
    }

    /**
     * Soft delete a draft procurement request
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete draft PR", description = "Soft delete a draft procurement request")
    public ResponseEntity<Void> deleteProcurementRequest(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt,
            @RequestHeader(value = "X-User-Id", required = false) UUID headerUserId) {

        UUID operatorId = SecurityUtils.getUserIdFromJwt(jwt);
        if (headerUserId != null) {
            SecurityUtils.validateUserIdMatch(headerUserId, jwt);
        }

        log.info("Deleting procurement request: {} by operator: {}", id, operatorId);

        procurementRequestService.deleteProcurementRequest(id, operatorId);

        return ResponseEntity.noContent().build();
    }
}
