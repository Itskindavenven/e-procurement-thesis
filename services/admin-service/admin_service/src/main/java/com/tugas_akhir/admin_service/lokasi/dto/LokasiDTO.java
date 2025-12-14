package com.tugas_akhir.admin_service.lokasi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class LokasiDTO {
    private UUID locationId;

    @NotBlank(message = "Location name is required")
    private String locationName;

    private String address;

    @NotBlank(message = "Location type is required")
    private String locationType;

    private String status;
}
