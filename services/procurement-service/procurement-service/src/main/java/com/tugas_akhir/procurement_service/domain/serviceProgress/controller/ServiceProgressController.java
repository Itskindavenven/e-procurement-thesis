package com.tugas_akhir.procurement_service.domain.serviceProgress.controller;

import com.tugas_akhir.procurement_service.domain.serviceProgress.dto.ServiceProgressDTOs.ServiceProgressDTO;
import com.tugas_akhir.procurement_service.domain.serviceProgress.service.ServiceProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/service-progress")
@RequiredArgsConstructor
public class ServiceProgressController {

    private final ServiceProgressService service;

    @PostMapping("/{progressId}/confirm")
    public ResponseEntity<ServiceProgressDTO> confirmCompletion(@PathVariable UUID progressId) {
        return ResponseEntity.ok(service.confirmServiceCompletion(progressId));
    }
}
