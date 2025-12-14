package com.tugas_akhir.admin_service.procurement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ProcurementCorrectionDTO {
    @NotNull(message = "Procurement ID is required")
    private UUID procurementId;

    @NotBlank(message = "Correction type is required")
    private String correctionType;

    private String oldValue;
    private String newValue;
    private String reason;
}
