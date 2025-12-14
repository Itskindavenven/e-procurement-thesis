package com.tugas_akhir.procurement_service.domain.termin.service;

import com.tugas_akhir.procurement_service.common.enums.TerminStatus;
import com.tugas_akhir.procurement_service.domain.termin.dto.TerminDTOs.*;
import com.tugas_akhir.procurement_service.domain.termin.entity.TerminDetails;
import com.tugas_akhir.procurement_service.domain.termin.repository.TerminDetailsRepository;
import com.tugas_akhir.procurement_service.exception.ResourceNotFoundException;
import com.tugas_akhir.procurement_service.exception.ValidationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for managing service termin review workflow
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TerminService {

    private final TerminDetailsRepository terminDetailsRepository;
    // TODO: Add event producer when implementing events
    // private final ProcurementEventProducer eventProducer;

    /**
     * List termins awaiting operator review
     */
    @Transactional(readOnly = true)
    public Page<TerminSummaryDTO> listPendingTermins(Pageable pageable) {
        log.info("Fetching pending termins for review");

        Page<TerminDetails> pendingTermins = terminDetailsRepository
                .findByStatus(TerminStatus.SUBMITTED_BY_VENDOR, pageable);

        return pendingTermins.map(this::mapToSummaryDTO);
    }

    /**
     * Get termin details
     */
    @Transactional(readOnly = true)
    public TerminDetailsResponseDTO getTerminDetails(UUID terminId) {
        log.info("Fetching termin details: {}", terminId);

        TerminDetails termin = terminDetailsRepository.findById(terminId)
                .orElseThrow(() -> new ResourceNotFoundException("Termin not found: " + terminId));

        return mapToResponseDTO(termin);
    }

    /**
     * Accept termin and forward to supervisor for approval
     */
    @Transactional
    public TerminActionResponseDTO acceptTermin(UUID terminId, AcceptTerminDTO request, UUID operatorId) {
        log.info("Accepting termin: {} by operator: {}", terminId, operatorId);

        TerminDetails termin = getTerminForReview(terminId);

        termin.setStatus(TerminStatus.REVIEWED_BY_OPERATOR);
        termin.setOperatorReviewNotes(request.getReviewNotes());
        termin.setOperatorReviewedAt(LocalDateTime.now());
        termin.setReviewedBy(operatorId);

        terminDetailsRepository.save(termin);

        // TODO: Publish event for supervisor approval
        // eventProducer.publishTerminReviewed(createTerminReviewedEvent(termin));

        log.info("Termin accepted and forwarded to supervisor: {}", terminId);

        return createActionResponse(terminId, TerminStatus.REVIEWED_BY_OPERATOR,
                "Termin accepted and forwarded to supervisor for approval");
    }

    /**
     * Request clarification from vendor
     */
    @Transactional
    public TerminActionResponseDTO requestClarification(UUID terminId, RequestClarificationDTO request,
            UUID operatorId) {
        log.info("Requesting clarification for termin: {} by operator: {}", terminId, operatorId);

        TerminDetails termin = getTerminForReview(terminId);

        termin.setStatus(TerminStatus.CLARIFICATION_REQUESTED);
        termin.setClarificationNotes(request.getClarificationNotes());
        termin.setOperatorReviewedAt(LocalDateTime.now());
        termin.setReviewedBy(operatorId);

        terminDetailsRepository.save(termin);

        // TODO: Publish event to notify vendor
        // eventProducer.publishTerminClarificationRequested(createClarificationEvent(termin));

        log.info("Clarification requested for termin: {}", terminId);

        return createActionResponse(terminId, TerminStatus.CLARIFICATION_REQUESTED,
                "Clarification requested from vendor");
    }

    /**
     * Request revision from vendor
     */
    @Transactional
    public TerminActionResponseDTO requestRevision(UUID terminId, RequestRevisionDTO request, UUID operatorId) {
        log.info("Requesting revision for termin: {} by operator: {}", terminId, operatorId);

        TerminDetails termin = getTerminForReview(terminId);

        termin.setStatus(TerminStatus.REVISION_REQUESTED);
        termin.setRevisionNotes(request.getRevisionNotes());
        termin.setOperatorReviewedAt(LocalDateTime.now());
        termin.setReviewedBy(operatorId);

        terminDetailsRepository.save(termin);

        // TODO: Publish event to notify vendor
        // eventProducer.publishTerminRevisionRequested(createRevisionEvent(termin));

        log.info("Revision requested for termin: {}", terminId);

        return createActionResponse(terminId, TerminStatus.REVISION_REQUESTED, "Revision requested from vendor");
    }

    // Helper methods

    private TerminDetails getTerminForReview(UUID terminId) {
        TerminDetails termin = terminDetailsRepository.findById(terminId)
                .orElseThrow(() -> new ResourceNotFoundException("Termin not found: " + terminId));

        // Only submitted termins can be reviewed
        if (termin.getStatus() != TerminStatus.SUBMITTED_BY_VENDOR) {
            throw new ValidationException("Only submitted termins can be reviewed");
        }

        return termin;
    }

    private TerminActionResponseDTO createActionResponse(UUID terminId, TerminStatus newStatus, String message) {
        return TerminActionResponseDTO.builder()
                .terminId(terminId)
                .newStatus(newStatus.name())
                .actionedAt(LocalDateTime.now())
                .message(message)
                .build();
    }

    private TerminSummaryDTO mapToSummaryDTO(TerminDetails termin) {
        long daysWaiting = java.time.Duration.between(
                termin.getVendorSubmittedAt(),
                LocalDateTime.now()).toDays();

        return TerminSummaryDTO.builder()
                .id(termin.getId())
                .procurementRequestId(termin.getProcurementRequest().getId())
                .prDescription(termin.getProcurementRequest().getDescription())
                .terminNumber(termin.getTerminNumber())
                .phaseName(termin.getPhaseName())
                .value(termin.getValue())
                .status(termin.getStatus().name())
                .vendorSubmittedAt(termin.getVendorSubmittedAt())
                .daysWaiting((int) daysWaiting)
                .build();
    }

    private TerminDetailsResponseDTO mapToResponseDTO(TerminDetails termin) {
        return TerminDetailsResponseDTO.builder()
                .id(termin.getId())
                .procurementRequestId(termin.getProcurementRequest().getId())
                .prDescription(termin.getProcurementRequest().getDescription())
                .terminNumber(termin.getTerminNumber())
                .phaseName(termin.getPhaseName())
                .value(termin.getValue())
                .deliverables(termin.getDeliverables())
                .status(termin.getStatus().name())
                .vendorSubmittedAt(termin.getVendorSubmittedAt())
                .operatorReviewedAt(termin.getOperatorReviewedAt())
                .supervisorDecidedAt(termin.getSupervisorDecidedAt())
                .clarificationNotes(termin.getClarificationNotes())
                .revisionNotes(termin.getRevisionNotes())
                .operatorReviewNotes(termin.getOperatorReviewNotes())
                .supervisorNotes(termin.getSupervisorNotes())
                .reviewedBy(termin.getReviewedBy())
                .approvedBy(termin.getApprovedBy())
                .createdAt(termin.getCreatedAt())
                .updatedAt(termin.getUpdatedAt())
                .build();
    }
}
