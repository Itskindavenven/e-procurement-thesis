package com.tugas_akhir.procurement_service.domain.receiving.controller;

import com.tugas_akhir.procurement_service.domain.receiving.dto.ReceivingDTOs.*;
import com.tugas_akhir.procurement_service.domain.receiving.service.ReceivingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller for goods/service receiving operations
 */
@Slf4j
@RestController
@RequestMapping("/api/operator/receiving")
@RequiredArgsConstructor
@Tag(name = "Operator - Receiving", description = "APIs for Operator to manage goods/service receiving")
public class ReceivingController {

    private final ReceivingService receivingService;

    /**
     * List POs awaiting receiving
     */
    @GetMapping("/pos")
    @Operation(summary = "List POs awaiting receiving", description = "List all purchase orders delivered and awaiting receiving confirmation")
    public ResponseEntity<Page<POAwaitingReceivingDTO>> listPOsAwaitingReceiving(
            @PageableDefault(size = 20, sort = "deliveredAt", direction = Sort.Direction.ASC) Pageable pageable) {

        log.info("Listing POs awaiting receiving");

        Page<POAwaitingReceivingDTO> response = receivingService.listPOsAwaitingReceiving(pageable);

        return ResponseEntity.ok(response);
    }

    /**
     * Get PO details for receiving
     */
    @GetMapping("/pos/{id}")
    @Operation(summary = "Get PO details for receiving", description = "Get full details of a purchase order to perform receiving")
    public ResponseEntity<POForReceivingDTO> getPOForReceiving(
            @PathVariable UUID id) {

        log.info("Fetching PO for receiving: {}", id);

        POForReceivingDTO response = receivingService.getPOForReceivingDetails(id);

        return ResponseEntity.ok(response);
    }

    /**
     * Accept full delivery
     */
    @PostMapping("/pos/{id}/accept")
    @Operation(summary = "Accept full delivery", description = "Accept complete delivery of all items in good condition")
    public ResponseEntity<ReceivingResponseDTO> acceptFullDelivery(
            @PathVariable UUID id,
            @Valid @RequestBody AcceptFullDeliveryDTO request,
            @RequestHeader("X-User-Id") UUID operatorId) {

        log.info("Accepting full delivery for PO: {} by operator: {}", id, operatorId);

        ReceivingResponseDTO response = receivingService.acceptFullDelivery(id, request, operatorId);

        return ResponseEntity.ok(response);
    }

    /**
     * Accept partial delivery
     */
    @PostMapping("/pos/{id}/partial")
    @Operation(summary = "Accept partial delivery", description = "Accept partial quantity delivery when not all items received")
    public ResponseEntity<ReceivingResponseDTO> acceptPartialDelivery(
            @PathVariable UUID id,
            @Valid @RequestBody AcceptPartialDeliveryDTO request,
            @RequestHeader("X-User-Id") UUID operatorId) {

        log.info("Accepting partial delivery for PO: {} by operator: {}", id, operatorId);

        ReceivingResponseDTO response = receivingService.acceptPartialDelivery(id, request, operatorId);

        return ResponseEntity.ok(response);
    }

    /**
     * Reject delivery
     */
    @PostMapping("/pos/{id}/reject")
    @Operation(summary = "Reject delivery", description = "Reject delivery completely due to quality or other issues")
    public ResponseEntity<ReceivingResponseDTO> rejectDelivery(
            @PathVariable UUID id,
            @Valid @RequestBody RejectDeliveryDTO request,
            @RequestHeader("X-User-Id") UUID operatorId) {

        log.info("Rejecting delivery for PO: {} by operator: {}", id, operatorId);

        ReceivingResponseDTO response = receivingService.rejectDelivery(id, request, operatorId);

        return ResponseEntity.ok(response);
    }

    /**
     * Return goods
     */
    @PostMapping("/pos/{id}/return")
    @Operation(summary = "Return goods", description = "Return goods to vendor after receiving due to defects or issues")
    public ResponseEntity<ReceivingResponseDTO> returnGoods(
            @PathVariable UUID id,
            @Valid @RequestBody ReturnGoodsDTO request,
            @RequestHeader("X-User-Id") UUID operatorId) {

        log.info("Returning goods for PO: {} by operator: {}", id, operatorId);

        ReceivingResponseDTO response = receivingService.returnGoods(id, request, operatorId);

        return ResponseEntity.ok(response);
    }
}
