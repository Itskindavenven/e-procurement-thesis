package com.tugas_akhir.admin_service.lokasi.controller;

import com.tugas_akhir.admin_service.lokasi.dto.LokasiDTO;
import com.tugas_akhir.admin_service.lokasi.service.LokasiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/locations")
@RequiredArgsConstructor
public class LokasiController {

    private final LokasiService lokasiService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LokasiDTO>> getAllLocations() {
        return ResponseEntity.ok(lokasiService.getAllLocations());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LokasiDTO> getLocationById(@PathVariable UUID id) {
        return ResponseEntity.ok(lokasiService.getLocationById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LokasiDTO> createLocation(@Valid @RequestBody LokasiDTO dto) {
        return new ResponseEntity<>(lokasiService.createLocation(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LokasiDTO> updateLocation(@PathVariable UUID id, @Valid @RequestBody LokasiDTO dto) {
        return ResponseEntity.ok(lokasiService.updateLocation(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateLocation(@PathVariable UUID id) {
        lokasiService.deactivateLocation(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/assign-operator")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignOperator(@PathVariable UUID id, @RequestBody Map<String, String> payload) {
        UUID operatorId = UUID.fromString(payload.get("operatorId"));
        lokasiService.assignOperator(id, operatorId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/assign-supervisor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignSupervisor(@PathVariable UUID id, @RequestBody Map<String, String> payload) {
        UUID supervisorId = UUID.fromString(payload.get("supervisorId"));
        lokasiService.assignSupervisor(id, supervisorId);
        return ResponseEntity.ok().build();
    }
}
