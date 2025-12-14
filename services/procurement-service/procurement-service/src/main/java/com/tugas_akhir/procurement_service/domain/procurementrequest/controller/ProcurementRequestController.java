package com.tugas_akhir.procurement_service.domain.procurementrequest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tugas_akhir.procurement_service.domain.procurementrequest.dto.ProcurementRequestDTO;
import com.tugas_akhir.procurement_service.domain.procurementrequest.service.ProcurementRequestService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/procurement-requests")
@RequiredArgsConstructor
public class ProcurementRequestController {

    private final ProcurementRequestService service;

    @PostMapping("/draft")
    public ResponseEntity<com.tugas_akhir.procurement_service.common.BaseResponse<ProcurementRequestDTO>> createDraft(
            @RequestBody ProcurementRequestDTO dto) {
        return ResponseEntity.ok(com.tugas_akhir.procurement_service.common.BaseResponse
                .success("Draft created successfully", service.createDraft(dto)));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<com.tugas_akhir.procurement_service.common.BaseResponse<ProcurementRequestDTO>> submitRequest(
            @PathVariable UUID id) {
        return ResponseEntity.ok(com.tugas_akhir.procurement_service.common.BaseResponse
                .success("Request submitted successfully", service.submitRequest(id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<com.tugas_akhir.procurement_service.common.BaseResponse<ProcurementRequestDTO>> getRequestById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(com.tugas_akhir.procurement_service.common.BaseResponse
                .success("Request fetched successfully", service.getRequestById(id)));
    }
}

