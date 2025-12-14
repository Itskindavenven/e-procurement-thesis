package com.tugas_akhir.procurement_service.domain.procurementorder.dto;

import com.tugas_akhir.procurement_service.common.enums.POStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Standalone DTO for PO Header - for backward compatibility
 * Prefer using PODTOs.POHeaderDTO in new code
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class POHeaderDTO {
    private UUID id;
    private UUID procurementRequestId;
    private String poNumber;
    private POStatus status;
    private LocalDateTime createdAt;
    private List<POItemDTO> items;
}
