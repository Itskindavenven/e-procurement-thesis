package com.tugas_akhir.procurement_service.domain.delivery.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for delivery details - standalone version
 * Note: ProcurementRequestDTOs also has a nested DeliveryDetailDTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDetailDTO {

    private UUID id;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    private UUID locationId;

    @NotNull(message = "Planned delivery date is required")
    @Future(message = "Delivery date must be in the future")
    private LocalDate plannedDeliveryDate;

    private String deliveryNotes;
}
