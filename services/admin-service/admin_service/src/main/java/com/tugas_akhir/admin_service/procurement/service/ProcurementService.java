package com.tugas_akhir.admin_service.procurement.service;

import com.tugas_akhir.admin_service.procurement.dto.InvestigationFlagDTO;
import com.tugas_akhir.admin_service.procurement.dto.ProcurementCorrectionDTO;
import com.tugas_akhir.admin_service.procurement.entity.AuditInvestigationFlag;
import com.tugas_akhir.admin_service.procurement.entity.ProcurementCorrection;
import com.tugas_akhir.admin_service.procurement.repository.AuditInvestigationFlagRepository;
import com.tugas_akhir.admin_service.procurement.repository.ProcurementCorrectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcurementService {

    private final ProcurementCorrectionRepository correctionRepository;
    private final AuditInvestigationFlagRepository flagRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public void correctAdministrativeData(ProcurementCorrectionDTO dto) {
        ProcurementCorrection correction = new ProcurementCorrection();
        correction.setProcurementId(dto.getProcurementId());
        correction.setCorrectionType(dto.getCorrectionType());
        correction.setOldValue(dto.getOldValue());
        correction.setNewValue(dto.getNewValue());
        correction.setReason(dto.getReason());
        correction.setCreatedAt(LocalDateTime.now());
        // createdBy should be set from SecurityContext, but for now we skip or pass it
        // in DTO if needed

        correctionRepository.save(correction);

        // Publish event
        kafkaTemplate.send("admin.procurement.metadata.updated", "procurement.metadata.updated",
                dto.getProcurementId().toString());
    }

    @Transactional
    public void flagForInvestigation(InvestigationFlagDTO dto) {
        AuditInvestigationFlag flag = new AuditInvestigationFlag();
        flag.setProcurementId(dto.getProcurementId());
        flag.setReason(dto.getReason());
        flag.setFlaggedBy("ADMIN"); // Should be dynamic
        flag.setFlaggedAt(LocalDateTime.now());
        flag.setStatus("OPEN");

        flagRepository.save(flag);

        // Publish event
        kafkaTemplate.send("admin.procurement.investigation.flagged", "procurement.investigation.flagged",
                dto.getProcurementId().toString());
    }

    @Transactional
    public void updateDocumentStatus(UUID procurementId, String newStatus) {
        // This logic would typically involve calling the Procurement Service to update
        // the status
        // For Admin Service, we might just log this action or send a command event

        kafkaTemplate.send("admin.procurement.events", "procurement.status.update.requested",
                procurementId.toString() + ":" + newStatus);
    }
}
