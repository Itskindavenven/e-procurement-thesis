package com.tugas_akhir.procurement_service.domain.serviceProgress.controller;

import com.tugas_akhir.procurement_service.domain.serviceProgress.dto.ServiceProgressDTOs.ServiceProgressDTO;
import com.tugas_akhir.procurement_service.domain.serviceProgress.service.ServiceProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/operator/service-completion")
@RequiredArgsConstructor
@Tag(name = "Operator - Service Completion", description = "APIs for confirming service completion")
@PreAuthorize("hasRole('OPERATOR')")
public class ServiceCompletionController {

    private final ServiceProgressService serviceProgressService;

    @PostMapping("/{progressId}/confirm")
    @Operation(summary = "Confirm Service Completion", description = "Confirm that the service has been completed satisfactorily")
    public ResponseEntity<ServiceProgressDTO> confirmCompletion(
            @PathVariable UUID progressId,
            @RequestHeader("X-User-Id") UUID operatorId) {

        log.info("Confirming service completion for progress: {} by operator: {}", progressId, operatorId);

        // Note: In real app, we validate operatorId against the PR/Service assignment
        return ResponseEntity.ok(serviceProgressService.confirmServiceCompletion(progressId));
    }
}
