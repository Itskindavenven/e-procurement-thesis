package com.tugasakhir.vendor_service.controller.vendor;

import com.tugasakhir.vendor_service.dto.vendor.VendorAccountDTO;
import com.tugasakhir.vendor_service.service.vendor.VendorAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/account")
@RequiredArgsConstructor
@Tag(name = "Vendor Account", description = "Vendor Account Management")
public class VendorAccountController {

    private final VendorAccountService service;

    @GetMapping
    @Operation(summary = "Get all vendors")
    public ResponseEntity<List<VendorAccountDTO>> getAllVendors() {
        return ResponseEntity.ok(service.getAllVendors());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vendor by ID")
    public ResponseEntity<VendorAccountDTO> getVendorById(@PathVariable String id) {
        return ResponseEntity.ok(service.getVendorById(id));
    }

    @PostMapping
    @Operation(summary = "Create new vendor")
    public ResponseEntity<VendorAccountDTO> createVendor(@RequestBody VendorAccountDTO dto) {
        return ResponseEntity.ok(service.createVendor(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update vendor")
    public ResponseEntity<VendorAccountDTO> updateVendor(@PathVariable String id, @RequestBody VendorAccountDTO dto) {
        return ResponseEntity.ok(service.updateVendor(id, dto));
    }
}
