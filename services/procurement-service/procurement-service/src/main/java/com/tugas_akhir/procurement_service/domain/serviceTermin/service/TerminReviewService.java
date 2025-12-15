package com.tugas_akhir.procurement_service.domain.serviceTermin.service;

import com.tugas_akhir.procurement_service.common.enums.TerminStatus;
import com.tugas_akhir.procurement_service.domain.termin.dto.TerminDTOs.TerminDetailsResponseDTO;
import com.tugas_akhir.procurement_service.domain.termin.entity.TerminDetails;
import com.tugas_akhir.procurement_service.domain.termin.repository.TerminDetailsRepository;
import com.tugas_akhir.procurement_service.mapper.ProcurementMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TerminReviewService {

    private final TerminDetailsRepository repository;
    private final ProcurementMapper mapper;
    private final com.tugas_akhir.procurement_service.event.producer.ProcurementEventProducer eventProducer;

    @Transactional
    public TerminDetailsResponseDTO approveTermin(UUID terminId) {
        TerminDetails entity = repository.findById(terminId)
                .orElseThrow(() -> new RuntimeException("Termin not found"));

        entity.setStatus(TerminStatus.REVIEWED);
        // Logic to notify supervisor or vendor
        TerminDetails saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    /**
     * Request revision from vendor for termin
     */
    @Transactional
    public TerminDetailsResponseDTO requestRevision(UUID terminId, String revisionNotes, UUID operatorId) {
        TerminDetails entity = repository.findById(terminId)
                .orElseThrow(() -> new RuntimeException("Termin not found"));

        // Validate current status
        if (entity.getStatus() != TerminStatus.SUBMITTED_BY_VENDOR
                && entity.getStatus() != TerminStatus.REVISED) {
            throw new IllegalStateException("Can only request revision for submitted or revised termins");
        }

        entity.setStatus(TerminStatus.REVISION_REQUESTED);
        entity.setRevisionNotes(revisionNotes);
        entity.setReviewedBy(operatorId);
        entity.setOperatorReviewedAt(java.time.LocalDateTime.now());

        TerminDetails saved = repository.save(entity);

        // Publish event to notify vendor about revision request
        eventProducer.publishTerminRevisionRequested(terminId, revisionNotes);

        return mapper.toDTO(saved);
    }

    /**
     * Request clarification from vendor for termin
     */
    @Transactional
    public TerminDetailsResponseDTO requestClarification(UUID terminId, String clarificationNotes, UUID operatorId) {
        TerminDetails entity = repository.findById(terminId)
                .orElseThrow(() -> new RuntimeException("Termin not found"));

        // Validate current status
        if (entity.getStatus() != TerminStatus.SUBMITTED_BY_VENDOR
                && entity.getStatus() != TerminStatus.REVISED) {
            throw new IllegalStateException("Can only request clarification for submitted or revised termins");
        }

        entity.setStatus(TerminStatus.CLARIFICATION_REQUESTED);
        entity.setClarificationNotes(clarificationNotes);
        entity.setReviewedBy(operatorId);
        entity.setOperatorReviewedAt(java.time.LocalDateTime.now());

        TerminDetails saved = repository.save(entity);

        // Publish event to notify vendor about clarification request
        eventProducer.publishTerminClarificationRequested(terminId,
                clarificationNotes);

        return mapper.toDTO(saved);
    }
}
