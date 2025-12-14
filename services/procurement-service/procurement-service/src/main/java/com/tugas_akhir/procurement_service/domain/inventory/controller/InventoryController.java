package com.tugas_akhir.procurement_service.domain.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tugas_akhir.procurement_service.common.BaseResponse;
import com.tugas_akhir.procurement_service.domain.inventory.entity.InventoryItem;
import com.tugas_akhir.procurement_service.domain.inventory.service.InventoryDomainService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryDomainService inventoryService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<InventoryItem>>> getAllItems() {
        return ResponseEntity.ok(BaseResponse.success("Fetched all items", inventoryService.getAllItems()));
    }

    @PostMapping("/manual-mutation")
    public ResponseEntity<BaseResponse<String>> manualMutation(@RequestParam UUID itemId,
            @RequestParam int adjustmentQty,
            @RequestParam String reason,
            @RequestParam String actor) {
        inventoryService.manualAdjustment(itemId, adjustmentQty, reason, actor);
        return ResponseEntity.ok(BaseResponse.success("Manual adjustment successful", null));
    }
}

