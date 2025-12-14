package com.tugasakhir.vendor_service.controller.delivery;

import com.tugasakhir.vendor_service.dto.delivery.DeliveryDTO;
import com.tugasakhir.vendor_service.service.delivery.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/vendor/delivery")
@RequiredArgsConstructor
@Tag(name = "Delivery Management", description = "Manage deliveries and shipping")
public class DeliveryController {

    private final DeliveryService service;

    @PostMapping
    @Operation(summary = "Create delivery")
    public ResponseEntity<DeliveryDTO> createDelivery(@RequestBody DeliveryDTO dto) {
        return ResponseEntity.ok(service.createDelivery(dto));
    }

    @PostMapping(value = "/{id}/packing-list", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload packing list")
    public ResponseEntity<Void> uploadPackingList(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        service.uploadPackingList(id, file);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{id}/pod", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload Proof of Delivery (POD)")
    public ResponseEntity<Void> uploadPOD(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        service.uploadPOD(id, file);
        return ResponseEntity.ok().build();
    }
}
