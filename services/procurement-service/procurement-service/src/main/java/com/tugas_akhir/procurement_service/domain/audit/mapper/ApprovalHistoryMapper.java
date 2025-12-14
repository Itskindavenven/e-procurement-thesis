package com.tugas_akhir.procurement_service.domain.audit.mapper;

import com.tugas_akhir.procurement_service.domain.audit.dto.ApprovalDTOs.*;
import com.tugas_akhir.procurement_service.domain.audit.entity.ApprovalHistory;
import com.tugas_akhir.procurement_service.domain.procurementrequest.dto.ProcurementRequestDTOs.ApprovalHistoryDTO;

import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for ApprovalHistory entity
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApprovalHistoryMapper {

    /**
     * Map ApprovalHistory entity to DTO for PR response
     */
    @Mapping(target = "approverName", ignore = true) // Populated by service from user service
    @Mapping(target = "role", expression = "java(entity.getRole().name())")
    @Mapping(target = "decision", expression = "java(entity.getDecision().name())")
    ApprovalHistoryDTO toDTO(ApprovalHistory entity);

    /**
     * Map list of ApprovalHistory entities to DTOs
     */
    List<ApprovalHistoryDTO> toDTOs(List<ApprovalHistory> entities);

    /**
     * Map ApprovalHistory to ApprovalResponseDTO
     */
    @Mapping(target = "approvalId", source = "id")
    @Mapping(target = "procurementRequestId", source = "procurementRequest.id")
    @Mapping(target = "approvedAt", source = "timestamp")
    @Mapping(target = "newStatus", ignore = true) // Set by service
    ApprovalResponseDTO toApprovalResponseDTO(ApprovalHistory entity);
}
