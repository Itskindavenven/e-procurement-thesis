package com.tugas_akhir.admin_service.vendor.controller;

import com.tugas_akhir.admin_service.vendor.dto.VendorDTO;
import com.tugas_akhir.admin_service.vendor.service.VendorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VendorDTO>> getAllVendorRegistrations() {
        return ResponseEntity.ok(vendorService.getAllVendorRegistrations());
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VendorDTO>> getPendingVendorRegistrations() {
        return ResponseEntity.ok(vendorService.getPendingVendorRegistrations());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorDTO> getVendorRegistrationById(@PathVariable UUID id) {
        return ResponseEntity.ok(vendorService.getVendorRegistrationById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Or maybe internal call from Vendor Service?
    public ResponseEntity<VendorDTO> createVendorRegistration(@Valid @RequestBody VendorDTO dto) {
        return ResponseEntity.ok(vendorService.createVendorRegistration(dto));
    }

    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorDTO> verifyVendor(
            @PathVariable UUID id,
            @RequestBody Map<String, String> payload) {
        String status = payload.get("status");
        String notes = payload.get("notes");
        return ResponseEntity.ok(vendorService.verifyVendor(id, status, notes));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateVendor(@PathVariable UUID id) {
        vendorService.deactivateVendor(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/notes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addEvaluationNote(@PathVariable UUID id, @RequestBody Map<String, String> payload) {
        String note = payload.get("note");
        vendorService.addEvaluationNote(id, note);
        return ResponseEntity.ok().build();
    }
}
