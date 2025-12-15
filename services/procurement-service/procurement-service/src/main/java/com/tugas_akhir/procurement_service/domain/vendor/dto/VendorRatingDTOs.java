package com.tugas_akhir.procurement_service.domain.vendor.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

public class VendorRatingDTOs {

    @Data
    @Builder
    public static class CreateVendorRatingDTO {
        private UUID vendorId; // Optional if inferred from PO
        private UUID poId;
        private Integer rating;
        private String comment;
    }

    @Data
    @Builder
    public static class VendorRatingResponseDTO {
        private UUID id;
        private UUID vendorId;
        private UUID poId;
        private Integer rating;
        private String comment;
        private LocalDateTime createdAt;
    }
}
