package com.tugasakhir.vendor_service.controller.catalog;

import com.tugasakhir.vendor_service.dto.catalog.VendorCatalogItemDTO;
import com.tugasakhir.vendor_service.service.catalog.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/catalog")
@RequiredArgsConstructor
@Tag(name = "Catalog Management", description = "Manage vendor catalog items")
public class CatalogController {

    private final CatalogService service;

    @GetMapping
    @Operation(summary = "Get catalog items by vendor")
    public ResponseEntity<List<VendorCatalogItemDTO>> getCatalogByVendor(@RequestParam String vendorId) {
        return ResponseEntity.ok(service.getCatalogByVendor(vendorId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get catalog item by ID")
    public ResponseEntity<VendorCatalogItemDTO> getItemById(@PathVariable String id) {
        return ResponseEntity.ok(service.getItemById(id));
    }

    @PostMapping
    @Operation(summary = "Create catalog item")
    public ResponseEntity<VendorCatalogItemDTO> createItem(@RequestBody VendorCatalogItemDTO dto) {
        return ResponseEntity.ok(service.createCatalogItem(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update catalog item")
    public ResponseEntity<VendorCatalogItemDTO> updateItem(@PathVariable String id,
            @RequestBody VendorCatalogItemDTO dto) {
        return ResponseEntity.ok(service.updateCatalogItem(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete catalog item")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        service.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/mass-update")
    @Operation(summary = "Mass update catalog items")
    public ResponseEntity<Void> massUpdate(@RequestBody List<VendorCatalogItemDTO> dtos) {
        service.massUpdateCatalogItems(dtos);
        return ResponseEntity.ok().build();
    }
}
