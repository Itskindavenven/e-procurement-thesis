package com.tugas_akhir.procurement_service.integration.vendor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "vendor-service", url = "${application.config.vendor-service-url}")
public interface VendorServiceClient {

    @GetMapping("/api/v1/vendors/{vendorId}/catalog")
    Object getVendorCatalog(@PathVariable("vendorId") UUID vendorId); // Replace Object with actual Catalog DTO if
                                                                      // available

    @GetMapping("/api/v1/vendors/{vendorId}/service-scopes")
    Object getServiceScopes(@PathVariable("vendorId") UUID vendorId);

    @GetMapping("/api/v1/vendors/{vendorId}/termin")
    Object getTerminOptions(@PathVariable("vendorId") UUID vendorId);

    @GetMapping("/api/v1/vendors/{vendorId}/documents")
    Object getVendorDocuments(@PathVariable("vendorId") UUID vendorId);
}

