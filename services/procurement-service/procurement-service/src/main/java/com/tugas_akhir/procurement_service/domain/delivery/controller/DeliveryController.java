package com.tugas_akhir.procurement_service.domain.delivery.controller;

import com.tugas_akhir.procurement_service.domain.procurementrequest.dto.ProcurementRequestDTOs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tugas_akhir.procurement_service.domain.delivery.service.DeliveryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/procurement-requests")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService service;

    @PostMapping("/{requestId}/delivery")
    public ResponseEntity<ProcurementRequestDTOs.DeliveryDetailDTO> setDeliveryDetail(@PathVariable UUID requestId,
                                                                                      @RequestBody ProcurementRequestDTOs.DeliveryDetailDTO dto) {
        return ResponseEntity.ok(service.setDeliveryDetail(requestId, dto));
    }
}

