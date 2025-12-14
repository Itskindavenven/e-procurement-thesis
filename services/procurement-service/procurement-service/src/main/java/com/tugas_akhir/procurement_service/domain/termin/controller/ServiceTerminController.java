package com.tugas_akhir.procurement_service.domain.termin.controller;

import com.tugas_akhir.procurement_service.domain.termin.dto.TerminDTOs.*;
import com.tugas_akhir.procurement_service.domain.termin.service.TerminService;

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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller for Operator service termin review operations
 */
@Slf4j
@RestController
@RequestMapping("/api/operator/service/termins")
@RequiredArgsConstructor
@Tag(name = "Operator - Service Termin", description = "APIs for Operator to review service termin submissions")
public class ServiceTerminController {

    private final TerminService terminService;

    /**
     * List termins awaiting review
     */
    @GetMapping
    @Operation(summary = "List pending termins", description = "List all service termins awaiting operator review")
    public ResponseEntity<Page<TerminSummaryDTO>> listPendingTermins(
            @PageableDefault(size = 20, sort = "vendorSubmittedAt", direction = Sort.Direction.ASC) Pageable pageable) {

        log.info("Listing pending termins for review");

        Page<TerminSummaryDTO> response = terminService.listPendingTermins(pageable);

        return ResponseEntity.ok(response);
    }

    /**
     * Get termin details for review
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get termin details", description = "Get full details of a service termin for review")
    public ResponseEntity<TerminDetailsResponseDTO> getTerminDetails(
            @PathVariable UUID id) {

        log.info("Fetching termin details: {}", id);

        TerminDetailsResponseDTO response = terminService.getTerminDetails(id);

        return ResponseEntity.ok(response);
    }

    /**
     * Accept termin and forward to supervisor
     */
    @PutMapping("/{id}/accept")
    @Operation(summary = "Accept termin", description = "Accept termin and forward to supervisor for payment approval")
    public ResponseEntity<TerminActionResponseDTO> acceptTermin(
            @PathVariable UUID id,
            @Valid @RequestBody AcceptTerminDTO request,
            @RequestHeader("X-User-Id") UUID operatorId) {

        log.info("Accepting termin: {} by operator: {}", id, operatorId);

        TerminActionResponseDTO response = terminService.acceptTermin(id, request, operatorId);

        return ResponseEntity.ok(response);
    }

    /**
     * Request clarification from vendor
     */
    @PutMapping("/{id}/clarify")
    @Operation(summary = "Request clarification", description = "Request clarification from vendor on termin details")
    public ResponseEntity<TerminActionResponseDTO> requestClarification(
            @PathVariable UUID id,
            @Valid @RequestBody RequestClarificationDTO request,
            @RequestHeader("X-User-Id") UUID operatorId) {

        log.info("Requesting clarification for termin: {} by operator: {}", id, operatorId);

        TerminActionResponseDTO response = terminService.requestClarification(id, request, operatorId);

        return ResponseEntity.ok(response);
    }

    /**
     * Request revision from vendor
     */
    @PutMapping("/{id}/revise")
    @Operation(summary = "Request revision", description = "Request revision from vendor on termin submission")
    public ResponseEntity<TerminActionResponseDTO> requestRevision(
            @PathVariable UUID id,
            @Valid @RequestBody RequestRevisionDTO request,
            @RequestHeader("X-User-Id") UUID operatorId) {

        log.info("Requesting revision for termin: {} by operator: {}", id, operatorId);

        TerminActionResponseDTO response = terminService.requestRevision(id, request, operatorId);

        return ResponseEntity.ok(response);
    }
}
