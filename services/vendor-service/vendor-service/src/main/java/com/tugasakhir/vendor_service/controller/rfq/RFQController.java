package com.tugasakhir.vendor_service.controller.rfq;

import com.tugasakhir.vendor_service.dto.rfq.RFQDTO;
import com.tugasakhir.vendor_service.service.rfq.RFQService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/rfq")
@RequiredArgsConstructor
@Tag(name = "RFQ Management", description = "View and manage RFQs")
public class RFQController {

    private final RFQService service;

    @GetMapping("/open")
    @Operation(summary = "Get open RFQs for vendor")
    public ResponseEntity<List<RFQDTO>> getOpenRFQs(@RequestParam(required = false) String vendorId) {
        return ResponseEntity.ok(service.getOpenRFQsForVendor(vendorId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get RFQ by ID")
    public ResponseEntity<RFQDTO> getRFQById(@PathVariable String id) {
        return ResponseEntity.ok(service.getRFQById(id));
    }

    @PostMapping("/{id}/decline")
    public ResponseEntity<Void> declineRFQ(@PathVariable String id, @RequestParam String vendorId,
            @RequestBody com.tugasakhir.vendor_service.dto.rfq.DeclineRequest request) {
        service.declineRFQ(id, vendorId, request.getReason());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/clarification")
    public ResponseEntity<Void> requestClarification(@PathVariable String id, @RequestParam String vendorId,
            @RequestBody com.tugasakhir.vendor_service.dto.rfq.ClarificationRequest request) {
        service.requestClarification(id, vendorId, request.getQuestion());
        return ResponseEntity.ok().build();
    }
}
