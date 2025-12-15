package com.tugas_akhir.procurement_service.domain.serviceTermin.controller;

import com.tugas_akhir.procurement_service.domain.serviceTermin.service.TerminReviewService;
import com.tugas_akhir.procurement_service.domain.termin.dto.TerminDTOs.TerminDetailsResponseDTO;
import com.tugas_akhir.procurement_service.domain.termin.dto.TerminDTOs.RequestClarificationDTO;
import com.tugas_akhir.procurement_service.domain.termin.dto.TerminDTOs.RequestRevisionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/termin")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SUPERVISOR', 'ADMIN')")
public class TerminReviewController {

    private final TerminReviewService service;

    @PostMapping("/{terminId}/approve")
    public ResponseEntity<TerminDetailsResponseDTO> approveTermin(@PathVariable UUID terminId) {
        return ResponseEntity.ok(service.approveTermin(terminId));
    }

    @PostMapping("/{terminId}/clarify")
    public ResponseEntity<TerminDetailsResponseDTO> requestClarification(
            @PathVariable UUID terminId,
            @Valid @RequestBody RequestClarificationDTO request,
            @RequestHeader("X-User-Id") UUID operatorId) {
        return ResponseEntity.ok(service.requestClarification(terminId, request.getClarificationNotes(), operatorId));
    }

    @PostMapping("/{terminId}/revise")
    public ResponseEntity<TerminDetailsResponseDTO> requestRevision(
            @PathVariable UUID terminId,
            @Valid @RequestBody RequestRevisionDTO request,
            @RequestHeader("X-User-Id") UUID operatorId) {
        return ResponseEntity.ok(service.requestRevision(terminId, request.getRevisionNotes(), operatorId));
    }
}
