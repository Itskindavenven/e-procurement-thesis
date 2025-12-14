package com.tugas_akhir.procurement_service.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event DTOs for Kafka messaging
 */
public class ProcurementEvents {

    /**
     * Event published when PR is submitted for approval
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PRSubmittedEvent {
        private UUID procurementRequestId;
        private UUID operatorId;
        private UUID vendorId;
        private String type; // GOODS or SERVICE
        private String description;
        private String priority;
        private BigDecimal totalAmount;
        private LocalDateTime deadline;
        private LocalDateTime submittedAt;
        private UUID locationId;

        // For notification
        private String operatorName;
        private String vendorName;
    }

    /**
     * Event published when PR is approved
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PRApprovedEvent {
        private UUID procurementRequestId;
        private UUID supervisorId;
        private UUID operatorId;
        private UUID vendorId;
        private String type;
        private BigDecimal totalAmount;
        private LocalDateTime approvedAt;
        private String approvalNote;

        // For PO creation
        private UUID locationId;
    }

    /**
     * Event published when PR is rejected
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PRRejectedEvent {
        private UUID procurementRequestId;
        private UUID supervisorId;
        private UUID operatorId;
        private LocalDateTime rejectedAt;
        private String rejectionReason;
    }

    /**
     * Event published when PR is returned for revision
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PRReturnedEvent {
        private UUID procurementRequestId;
        private UUID supervisorId;
        private UUID operatorId;
        private LocalDateTime returnedAt;
        private String returnReason;
    }

    /**
     * Event published for auto-reminder (24h)
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PRReminderEvent {
        private UUID procurementRequestId;
        private UUID supervisorId;
        private UUID operatorId;
        private LocalDateTime submittedAt;
        private Integer hoursWaiting;
    }

    /**
     * Event published for auto-escalation (48h)
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PRESCalatedEvent {
        private UUID procurementRequestId;
        private UUID supervisorId;
        private UUID operatorId;
        private LocalDateTime submittedAt;
        private Integer hoursWaiting;
        private String escalationReason;
    }

    /**
     * Event published when PO is created (stub for Phase 3)
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class POCreatedEvent {
        private UUID poId;
        private UUID procurementRequestId;
        private String poNumber;
        private UUID vendorId;
        private BigDecimal totalAmount;
        private LocalDateTime createdAt;
    }

    /**
     * Event published when inventory stock falls below threshold
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockAlertEvent {
        private String sku;
        private String itemName;
        private Integer currentQuantity;
        private Integer minimumThreshold;
        private LocalDateTime alertTime;
    }

    // ========== Phase 3: Inventory Integration Events ==========

    /**
     * Event published when goods are fully received and accepted
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoodsReceivedEvent {
        private UUID receivingId;
        private UUID poId;
        private String poNumber;
        private UUID prId;
        private UUID vendorId;
        private UUID locationId;
        private String condition; // GOOD, DAMAGED, INCOMPLETE
        private LocalDateTime receivedAt;
        private UUID receivedBy;
        private String notes;

        // For inventory update
        private java.util.List<ReceivedItem> items;
    }

    /**
     * Event published when partial goods are received
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PartialGoodsReceivedEvent {
        private UUID receivingId;
        private UUID poId;
        private String poNumber;
        private Integer receivedQuantity;
        private Integer expectedQuantity;
        private String condition;
        private LocalDateTime receivedAt;
        private UUID receivedBy;
        private String notes;

        // For inventory update
        private java.util.List<ReceivedItem> items;
    }

    /**
     * Event published when delivery is rejected
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeliveryRejectedEvent {
        private UUID receivingId;
        private UUID poId;
        private String poNumber;
        private UUID vendorId;
        private String rejectionReason;
        private LocalDateTime rejectedAt;
        private UUID rejectedBy;

        // For vendor notification
        private String operatorName;
        private String vendorName;
    }

    /**
     * Event published when goods are returned to vendor
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoodsReturnedEvent {
        private UUID receivingId;
        private UUID poId;
        private String poNumber;
        private UUID vendorId;
        private Integer returnQuantity;
        private String condition;
        private String returnReason;
        private LocalDateTime returnedAt;
        private UUID returnedBy;

        // For inventory adjustment
        private java.util.List<ReceivedItem> items;
    }

    /**
     * Helper class for item details in inventory events
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceivedItem {
        private UUID itemId;
        private String itemName;
        private String sku;
        private Integer quantity;
        private String condition;
        private UUID locationId;
    }
}
