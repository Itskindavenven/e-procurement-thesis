package com.tugas_akhir.admin_service.masterdata.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class MasterDataDTO {
    private UUID itemId;

    @NotBlank(message = "Name is required")
    private String name;

    private String code;
    private String category;
    private String unit;
    private String status;
}
