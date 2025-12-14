package com.tugas_akhir.procurement_service.domain.procurementorder.dto;

import com.tugas_akhir.procurement_service.common.enums.POItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Standalone DTO for PO Item - for backward compatibility
 * Prefer using PODTOs.POItemDTO in new code
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class POItemDTO {
    private UUID id;
    private UUID poHeaderId;
    private String itemName;
    private Integer quantity;
    private POItemStatus status;
}
