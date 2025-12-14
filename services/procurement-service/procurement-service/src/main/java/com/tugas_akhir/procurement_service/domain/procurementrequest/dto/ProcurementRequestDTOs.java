package com.tugas_akhir.procurement_service.domain.procurementrequest.dto;

import com.tugas_akhir.procurement_service.common.enums.ProcurementPriority;
import com.tugas_akhir.procurement_service.common.enums.ProcurementType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTOs for Procurement Request operations
 */
public class ProcurementRequestDTOs {

    /**
     * Request DTO for creating a new procurement request
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateProcurementRequestDTO {

        @NotNull(message = "Vendor ID is required")
        private UUID vendorId;

        @NotNull(message = "Procurement type is required")
        private ProcurementType type;

        @NotBlank(message = "Description is required")
        private String description;

        private ProcurementPriority priority;

        @NotNull(message = "Deadline is required")
        @Future(message = "Deadline must be in the future")
        private LocalDateTime deadline;

        private UUID locationId;

        @Valid
        @NotEmpty(message = "At least one item is required")
        private List<ProcurementItemDTO> items;

        @Valid
        private DeliveryDetailDTO deliveryDetail;

        private List<AdditionalDocumentDTO> documents;
    }

    /**
     * Request DTO for updating a draft procurement request
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProcurementRequestDTO {

        private UUID vendorId;
        private String description;
        private ProcurementPriority priority;
        private LocalDateTime deadline;
        private UUID locationId;

        @Valid
        private List<ProcurementItemDTO> items;

        @Valid
        private DeliveryDetailDTO deliveryDetail;

        private List<AdditionalDocumentDTO> documents;
    }

    /**
     * Response DTO for procurement request
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcurementRequestResponseDTO {

        private UUID id;
        private UUID operatorId;
        private UUID vendorId;
        private String vendorName; // From vendor service
        private UUID locationId;
        private String locationName; // From master data service
        private ProcurementType type;
        private String description;
        private ProcurementPriority priority;
        private String status;
        private LocalDateTime deadline;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private UUID createdBy;
        private UUID updatedBy;

        private List<ProcurementItemDTO> items;
        private DeliveryDetailDTO deliveryDetail;
        private List<AdditionalDocumentDTO> documents;
        private List<ApprovalHistoryDTO> approvalHistory;

        private BigDecimal totalAmount; // Calculated from items
    }

    /**
     * DTO for procurement item line
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcurementItemDTO {

        private UUID id;

        @NotNull(message = "Vendor catalog ID is required")
        private UUID vendorCatalogId;

        @NotBlank(message = "Item name is required")
        private String itemName;

        @NotNull(message = "Quantity is required")
        private Integer quantity;

        @NotNull(message = "Unit price is required")
        private BigDecimal unitPrice;

        private BigDecimal taxPpn;
        private BigDecimal subtotal;
    }

    /**
     * DTO for delivery details
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeliveryDetailDTO {

        private UUID id;

        @NotBlank(message = "Delivery address is required")
        private String deliveryAddress;

        private UUID locationId;

        @NotNull(message = "Planned delivery date is required")
        @Future(message = "Delivery date must be in the future")
        private LocalDate plannedDeliveryDate;

        private String deliveryNotes;
    }

    /**
     * DTO for additional documents
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdditionalDocumentDTO {

        private UUID id;
        private String fileName;
        private String fileType;
        private Long fileSize;
        private String fileUrl;
        private LocalDateTime uploadedAt;
    }

    /**
     * DTO for approval history
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApprovalHistoryDTO {

        private UUID id;
        private UUID approverId;
        private String approverName; // From user service
        private String role;
        private String decision;
        private String note;
        private LocalDateTime timestamp;
    }

    /**
     * Request DTO for submitting PR for approval
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubmitProcurementRequestDTO {

        private String submitNotes;
    }

    /**
     * Summary DTO for list views
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcurementRequestSummaryDTO {

        private UUID id;
        private String vendorName;
        private ProcurementType type;
        private String description;
        private ProcurementPriority priority;
        private String status;
        private LocalDateTime deadline;
        private BigDecimal totalAmount;
        private LocalDateTime createdAt;
        private Integer itemCount;
    }
}
