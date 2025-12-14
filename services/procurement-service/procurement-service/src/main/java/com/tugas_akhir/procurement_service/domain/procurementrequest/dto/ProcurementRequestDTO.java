package com.tugas_akhir.procurement_service.domain.procurementrequest.dto;

import com.tugas_akhir.procurement_service.domain.delivery.dto.DeliveryDetailDTO;
import com.tugas_akhir.procurement_service.domain.procurementorder.dto.POHeaderDTO;
import com.tugas_akhir.procurement_service.dto.TerminDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.tugas_akhir.procurement_service.common.enums.ProcurementPriority;
import com.tugas_akhir.procurement_service.common.enums.ProcurementStatus;
import com.tugas_akhir.procurement_service.common.enums.ProcurementType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcurementRequestDTO {
    private UUID id;
    private UUID operatorId;
    private UUID vendorId;
    private ProcurementType type;
    private String description;
    private ProcurementPriority priority;
    private ProcurementStatus status;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProcurementItemDTO> items;
    private List<DeliveryDetailDTO> deliveryDetails;
    private List<ProcurementRequestDTOs.AdditionalDocumentDTO> documents;
    private List<TerminDetailsDTO> terminDetails;
    private List<ProcurementRequestDTOs.ApprovalHistoryDTO> approvalHistories;
    private List<POHeaderDTO> poHeaders;
}

