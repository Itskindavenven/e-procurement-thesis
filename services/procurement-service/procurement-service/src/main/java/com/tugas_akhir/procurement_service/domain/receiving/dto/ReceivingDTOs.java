package com.tugas_akhir.procurement_service.domain.receiving.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTOs for goods/service receiving workflow
 */
public class ReceivingDTOs {

    /**
     * DTO for PO summary awaiting receiving
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class POAwaitingReceivingDTO {
        private UUID poId;
        private String poNumber;
        private UUID prId;
        private String prDescription;
        private String vendorName;
        private Integer totalQuantity;
        private LocalDateTime deliveredAt;
        private int daysWaiting;
    }

    /**
     * DTO for full PO details for receiving
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class POForReceivingDTO {
        private UUID poId;
        private String poNumber;
        private UUID vendorId;
        private String vendorName;
        private String status;
        private LocalDateTime deliveredAt;
        // PO items details would be included here
    }

    /**
     * DTO to accept full delivery
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AcceptFullDeliveryDTO {
        @NotBlank(message = "Condition is required")
        private String condition; // GOOD, DAMAGED, INCOMPLETE

        private String notes;
    }

    /**
     * DTO to accept partial delivery
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AcceptPartialDeliveryDTO {
        @NotNull(message = "Received quantity is required")
        @Min(value = 1, message = "Received quantity must be at least 1")
        private Integer receivedQuantity;

        @NotBlank(message = "Condition is required")
        private String condition;

        private String notes;
    }

    /**
     * DTO to reject delivery
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RejectDeliveryDTO {
        @NotBlank(message = "Rejection reason is required")
        private String rejectionReason;

        private String notes;
    }

    /**
     * DTO to return goods
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReturnGoodsDTO {
        @NotNull(message = "Return quantity is required")
        @Min(value = 1, message = "Return quantity must be at least 1")
        private Integer returnQuantity;

        @NotBlank(message = "Return reason is required")
        private String returnReason;

        private String condition;
        private String notes;
    }

    /**
     * Response after receiving action
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceivingResponseDTO {
        private UUID receivingId;
        private UUID poId;
        private String status;
        private LocalDateTime receivedAt;
        private String message;
    }

    /**
     * Full receiving record details
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceivingRecordDTO {
        private UUID id;
        private UUID poId;
        private String poNumber;
        private Integer receivedQuantity;
        private String condition;
        private String status;
        private LocalDateTime receivedAt;
        private UUID receivedBy;
        private String receivedByName;
        private String notes;
        private String rejectionReason;
    }
}
