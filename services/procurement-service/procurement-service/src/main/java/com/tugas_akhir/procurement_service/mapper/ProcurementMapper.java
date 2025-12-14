package com.tugas_akhir.procurement_service.mapper;

import com.tugas_akhir.procurement_service.domain.additionaldocument.entity.AdditionalDocument;
import com.tugas_akhir.procurement_service.domain.audit.entity.ApprovalHistory;
import com.tugas_akhir.procurement_service.domain.delivery.entity.DeliveryDetail;
import com.tugas_akhir.procurement_service.domain.procurementorder.dto.POHeaderDTO;
import com.tugas_akhir.procurement_service.domain.procurementorder.dto.POItemDTO;
import com.tugas_akhir.procurement_service.domain.procurementorder.entity.POHeader;
import com.tugas_akhir.procurement_service.domain.procurementorder.entity.POItem;
import com.tugas_akhir.procurement_service.domain.procurementrequest.dto.ProcurementItemDTO;
import com.tugas_akhir.procurement_service.domain.procurementrequest.dto.ProcurementRequestDTO;
import com.tugas_akhir.procurement_service.domain.procurementrequest.dto.ProcurementRequestDTOs;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementItem;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;
import com.tugas_akhir.procurement_service.domain.serviceProgress.entity.ServiceProgress;
import com.tugas_akhir.procurement_service.domain.serviceProgress.dto.ServiceProgressDTOs;
import com.tugas_akhir.procurement_service.domain.termin.dto.TerminDTOs;
import com.tugas_akhir.procurement_service.domain.termin.entity.TerminDetails;
import org.mapstruct.Mapper;

import com.tugas_akhir.procurement_service.dto.*;
import com.tugas_akhir.procurement_service.common.*;

@Mapper(componentModel = "spring")
public interface ProcurementMapper {
    ProcurementRequestDTO toDTO(ProcurementRequest entity);

    ProcurementRequest toEntity(ProcurementRequestDTO dto);

    ProcurementItemDTO toDTO(ProcurementItem entity);

    ProcurementItem toEntity(ProcurementItemDTO dto);

    ProcurementRequestDTOs.DeliveryDetailDTO toDTO(DeliveryDetail entity);

    DeliveryDetail toEntity(ProcurementRequestDTOs.DeliveryDetailDTO dto);

    ProcurementRequestDTOs.AdditionalDocumentDTO toDTO(AdditionalDocument entity);

    AdditionalDocument toEntity(ProcurementRequestDTOs.AdditionalDocumentDTO dto);

    TerminDTOs.TerminDetailsResponseDTO toDTO(TerminDetails entity);

    TerminDetails toEntity(TerminDTOs.TerminDetailsResponseDTO dto);

    ServiceProgressDTOs.ServiceProgressDTO toDTO(ServiceProgress entity);

    ServiceProgress toEntity(ServiceProgressDTOs.ServiceProgressDTO dto);

    ProcurementRequestDTOs.ApprovalHistoryDTO toDTO(ApprovalHistory entity);

    ApprovalHistory toEntity(ProcurementRequestDTOs.ApprovalHistoryDTO dto);

    POHeaderDTO toDTO(POHeader entity);

    POHeader toEntity(POHeaderDTO dto);

    POItemDTO toDTO(POItem entity);

    POItem toEntity(POItemDTO dto);
}
