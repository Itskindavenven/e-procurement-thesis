package com.tugas_akhir.admin_service.procurement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class InvestigationFlagDTO {
    @NotNull(message = "Procurement ID is required")
    private UUID procurementId;

    @NotBlank(message = "Reason is required")
    private String reason;

    private String flaggedBy;
    private String status;
}
