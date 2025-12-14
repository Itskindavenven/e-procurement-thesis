package com.tugas_akhir.admin_service.procurement.controller;

import com.tugas_akhir.admin_service.procurement.dto.InvestigationFlagDTO;
import com.tugas_akhir.admin_service.procurement.dto.ProcurementCorrectionDTO;
import com.tugas_akhir.admin_service.procurement.service.ProcurementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/procurement")
@RequiredArgsConstructor
public class ProcurementController {

    private final ProcurementService procurementService;

    @PostMapping("/correct")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> correctAdministrativeData(@Valid @RequestBody ProcurementCorrectionDTO dto) {
        procurementService.correctAdministrativeData(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/flag")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> flagForInvestigation(@Valid @RequestBody InvestigationFlagDTO dto) {
        procurementService.flagForInvestigation(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateDocumentStatus(@PathVariable UUID id, @RequestBody Map<String, String> payload) {
        String newStatus = payload.get("status");
        procurementService.updateDocumentStatus(id, newStatus);
        return ResponseEntity.ok().build();
    }
}
