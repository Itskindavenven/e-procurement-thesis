package com.tugas_akhir.admin_service.vendor.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class VendorDTO {
    private UUID registrationId;

    @NotNull(message = "Vendor ID is required")
    private UUID vendorId;

    private String documentUrl;
    private String verificationStatus;
    private String adminNotes;
}
