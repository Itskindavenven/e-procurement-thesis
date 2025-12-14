package com.tugas_akhir.admin_service.masterdata.controller;

import com.tugas_akhir.admin_service.masterdata.dto.MasterDataDTO;
import com.tugas_akhir.admin_service.masterdata.service.MasterDataService;
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
@RequestMapping("/api/admin/master-data")
@RequiredArgsConstructor
public class MasterDataController {

    private final MasterDataService masterDataService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MasterDataDTO>> getAllMasterData() {
        return ResponseEntity.ok(masterDataService.getAllMasterData());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MasterDataDTO> getMasterDataById(@PathVariable UUID id) {
        return ResponseEntity.ok(masterDataService.getMasterDataById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MasterDataDTO> createMasterData(@Valid @RequestBody MasterDataDTO dto) {
        return new ResponseEntity<>(masterDataService.createMasterData(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MasterDataDTO> updateMasterData(@PathVariable UUID id,
            @Valid @RequestBody MasterDataDTO dto) {
        return ResponseEntity.ok(masterDataService.updateMasterData(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMasterData(@PathVariable UUID id) {
        masterDataService.deleteMasterData(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/relations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createRelation(@RequestBody Map<String, String> payload) {
        UUID parentId = UUID.fromString(payload.get("parentId"));
        UUID childId = UUID.fromString(payload.get("childId"));
        String relationType = payload.get("relationType");
        masterDataService.createRelation(parentId, childId, relationType);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sync")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> syncMasterData(@RequestBody Map<String, String> payload) {
        String targetService = payload.get("targetService");
        masterDataService.syncMasterData(targetService);
        return ResponseEntity.ok().build();
    }
}
