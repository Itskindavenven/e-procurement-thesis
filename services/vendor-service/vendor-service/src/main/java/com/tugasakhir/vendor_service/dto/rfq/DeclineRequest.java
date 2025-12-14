package com.tugasakhir.vendor_service.dto.rfq;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeclineRequest {
    @NotBlank(message = "Reason is required")
    private String reason;
}
