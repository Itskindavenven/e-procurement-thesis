package com.tugas_akhir.procurement_service.domain.procurementrequest.controller;

import com.tugas_akhir.procurement_service.domain.procurementrequest.service.ProcurementItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tugas_akhir.procurement_service.domain.procurementrequest.dto.ProcurementItemDTO;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/procurement-requests") // Nested or separate? Sticking to separate endpoint for items
                                                // potentially or nested
@RequiredArgsConstructor
public class ProcurementItemController {

    private final ProcurementItemService service;

    @PostMapping("/{requestId}/items")
    public ResponseEntity<ProcurementItemDTO> addItem(@PathVariable UUID requestId,
            @RequestBody ProcurementItemDTO dto) {
        return ResponseEntity.ok(service.addItem(requestId, dto));
    }

    @GetMapping("/{requestId}/items")
    public ResponseEntity<List<ProcurementItemDTO>> getItems(@PathVariable UUID requestId) {
        return ResponseEntity.ok(service.getItemsByRequestId(requestId));
    }
}

