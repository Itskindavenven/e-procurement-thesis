package com.tugasakhir.vendor_service.dto.rfq;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClarificationRequest {
    @NotBlank(message = "Question is required")
    private String question;
}
