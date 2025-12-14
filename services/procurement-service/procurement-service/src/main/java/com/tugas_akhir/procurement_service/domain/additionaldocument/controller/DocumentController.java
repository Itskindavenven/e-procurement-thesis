package com.tugas_akhir.procurement_service.domain.additionaldocument.controller;

import com.tugas_akhir.procurement_service.domain.additionaldocument.service.DocumentService;
import com.tugas_akhir.procurement_service.domain.procurementrequest.dto.ProcurementRequestDTOs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequestMapping("/api/v1/procurement-requests")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService service;

    @PostMapping("/{requestId}/documents")
    public ResponseEntity<ProcurementRequestDTOs.AdditionalDocumentDTO> uploadDocument(@PathVariable UUID requestId,
                                                                                       @RequestBody ProcurementRequestDTOs.AdditionalDocumentDTO dto) {
        return ResponseEntity.ok(service.uploadDocument(requestId, dto));
    }
}

