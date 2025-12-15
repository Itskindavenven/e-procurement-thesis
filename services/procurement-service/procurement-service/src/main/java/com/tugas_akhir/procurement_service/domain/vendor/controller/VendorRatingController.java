package com.tugas_akhir.procurement_service.domain.vendor.controller;

import com.tugas_akhir.procurement_service.domain.vendor.dto.VendorRatingDTOs.*;
import com.tugas_akhir.procurement_service.domain.vendor.service.VendorRatingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/operator/vendor-ratings")
@RequiredArgsConstructor
@Tag(name = "Operator - Vendor Rating", description = "APIs for rating vendors")
@PreAuthorize("hasRole('OPERATOR')")
public class VendorRatingController {

    private final VendorRatingService vendorRatingService;

    /**
     * Submit vendor rating
     */
    @PostMapping
    @Operation(summary = "Submit vendor rating", description = "Rate a vendor after PO completion")
    public ResponseEntity<VendorRatingResponseDTO> submitRating(
            @Valid @RequestBody CreateVendorRatingDTO request,
            @RequestHeader("X-User-Id") UUID operatorId) {

        log.info("Received rating submission for PO: {}", request.getPoId());

        VendorRatingResponseDTO response = vendorRatingService.submitRating(request, operatorId);

        return ResponseEntity.ok(response);
    }

    /**
     * Get vendor rating (avg)
     */
    @GetMapping("/{vendorId}/average")
    @Operation(summary = "Get vendor average rating", description = "Get average rating for a specific vendor")
    public ResponseEntity<Double> getAverageRating(@PathVariable UUID vendorId) {
        return ResponseEntity.ok(vendorRatingService.getAverageRating(vendorId));
    }
}
