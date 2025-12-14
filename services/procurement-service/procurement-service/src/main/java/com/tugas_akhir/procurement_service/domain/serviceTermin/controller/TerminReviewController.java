package com.tugas_akhir.procurement_service.domain.serviceTermin.controller;

import com.tugas_akhir.procurement_service.domain.serviceTermin.service.TerminReviewService;
import com.tugas_akhir.procurement_service.domain.termin.dto.TerminDTOs.TerminDetailsResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/termin")
@RequiredArgsConstructor
public class TerminReviewController {

    private final TerminReviewService service;

    @PostMapping("/{terminId}/approve")
    public ResponseEntity<TerminDetailsResponseDTO> approveTermin(@PathVariable UUID terminId) {
        return ResponseEntity.ok(service.approveTermin(terminId));
    }
}
