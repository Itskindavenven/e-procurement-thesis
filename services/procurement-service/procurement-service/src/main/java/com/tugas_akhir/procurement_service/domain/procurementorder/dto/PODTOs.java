package com.tugas_akhir.procurement_service.domain.procurementorder.dto;

import com.tugas_akhir.procurement_service.common.enums.POItemStatus;
import com.tugas_akhir.procurement_service.common.enums.POStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTOs for Purchase Order management
 */
public class PODTOs {

    /**
     * Response DTO for PO Header
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class POHeaderDTO {
        private UUID id;
        private UUID procurementRequestId;
        private String poNumber;
        private POStatus status;
        private LocalDateTime createdAt;
        private List<POItemDTO> items;
    }

    /**
     * Response DTO for PO Item
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class POItemDTO {
        private UUID id;
        private UUID poHeaderId;
        private String itemName;
        private Integer quantity;
        private POItemStatus status;
    }

    /**
     * Request DTO for creating PO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreatePORequest {
        private UUID procurementRequestId;
        private List<CreatePOItemRequest> items;
    }

    /**
     * Request DTO for creating PO Item
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreatePOItemRequest {
        private String itemName;
        private Integer quantity;
    }
}
