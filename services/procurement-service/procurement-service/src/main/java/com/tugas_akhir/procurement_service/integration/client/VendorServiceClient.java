package com.tugas_akhir.procurement_service.integration.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

/**
 * Feign client for Vendor Service
 * Phase 1: Validation endpoints
 */
@FeignClient(name = "vendor-service", url = "${integration.vendor-service.url:http://localhost:8081}")
public interface VendorServiceClient {

    /**
     * Check if vendor is active
     */
    @GetMapping("/api/vendors/{vendorId}/active")
    Boolean isVendorActive(@PathVariable("vendorId") UUID vendorId);

    /**
     * Check if vendor has active catalog (for goods procurement)
     */
    @GetMapping("/api/vendors/{vendorId}/catalog/exists")
    Boolean hasActiveCatalog(@PathVariable("vendorId") UUID vendorId);

    /**
     * Get vendor details
     */
    @GetMapping("/api/vendors/{vendorId}")
    VendorDTO getVendorDetails(@PathVariable("vendorId") UUID vendorId);

    /**
     * DTO for vendor details
     */
    record VendorDTO(
            UUID id,
            String name,
            String contactPerson,
            String email,
            String phone,
            String address,
            Boolean isActive) {
    }
}
