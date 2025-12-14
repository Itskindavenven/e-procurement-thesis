package com.tugas_akhir.procurement_service.domain.audit.service;

import com.tugas_akhir.procurement_service.common.enums.ApprovalDecision;
import com.tugas_akhir.procurement_service.common.enums.ApprovalRole;
import com.tugas_akhir.procurement_service.common.enums.ProcurementStatus;
import com.tugas_akhir.procurement_service.domain.audit.dto.ApprovalDTOs.*;
import com.tugas_akhir.procurement_service.domain.audit.entity.ApprovalHistory;
import com.tugas_akhir.procurement_service.domain.audit.repository.ApprovalHistoryRepository;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;
import com.tugas_akhir.procurement_service.domain.procurementrequest.repository.ProcurementRequestRepository;
import com.tugas_akhir.procurement_service.event.producer.ProcurementEventProducer;
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
 * Service for handling Supervisor approval workflows
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApprovalService {

    private final ProcurementRequestRepository procurementRequestRepository;
    private final ApprovalHistoryRepository approvalHistoryRepository;
    private final ProcurementEventProducer eventProducer;

    /**
     * Get list of pending approvals for supervisor
     */
    @Transactional(readOnly = true)
    public Page<PendingApprovalDTO> getPendingApprovals(Pageable pageable) {
        log.info("Fetching pending approvals");

        Page<ProcurementRequest> pendingPRs = procurementRequestRepository
                .findByStatus(ProcurementStatus.SUBMITTED, pageable);

        return pendingPRs.map(this::mapToPendingApprovalDTO);
    }

    /**
     * Approve procurement request
     */
    @Transactional
    public ApprovalResponseDTO approveProcurementRequest(UUID prId, ApprovalRequestDTO request, UUID supervisorId) {
        log.info("Approving procurement request: {} by supervisor: {}", prId, supervisorId);

        if (request.getDecision() != ApprovalDecision.APPROVE) {
            throw new ValidationException("This endpoint only handles approvals");
        }

        ProcurementRequest pr = getProcurementRequestForApproval(prId);

        // Create approval record
        ApprovalHistory approval = createApprovalRecord(pr, supervisorId, ApprovalRole.SUPERVISOR, request);
        approvalHistoryRepository.save(approval);

        // Update PR status
        pr.setStatus(ProcurementStatus.APPROVED);
        pr.setUpdatedBy(supervisorId);
        procurementRequestRepository.save(pr);

        // Publish PR approved event for PO creation
        // TODO: Publish event
        // eventProducer.sendPrApprovedEvent(createPRApprovedEvent(pr, supervisorId,
        // request.getNote()));

        log.info("Procurement request approved successfully: {}", prId);

        return createApprovalResponse(approval.getId(), pr, ApprovalDecision.APPROVE);
    }

    /**
     * Reject procurement request
     */
    @Transactional
    public ApprovalResponseDTO rejectProcurementRequest(UUID prId, ApprovalRequestDTO request, UUID supervisorId) {
        log.info("Rejecting procurement request: {} by supervisor: {}", prId, supervisorId);

        if (request.getDecision() != ApprovalDecision.REJECT) {
            throw new ValidationException("This endpoint only handles rejections");
        }

        if (request.getNote() == null || request.getNote().isBlank()) {
            throw new ValidationException("Rejection reason is required");
        }

        ProcurementRequest pr = getProcurementRequestForApproval(prId);

        // Create approval record
        ApprovalHistory approval = createApprovalRecord(pr, supervisorId, ApprovalRole.SUPERVISOR, request);
        approvalHistoryRepository.save(approval);

        // Update PR status
        pr.setStatus(ProcurementStatus.REJECTED);
        pr.setUpdatedBy(supervisorId);
        procurementRequestRepository.save(pr);

        // Publish PR rejected event for notification
        // TODO: Publish event
        // eventProducer.sendPrRejectedEvent(createPRRejectedEvent(pr, supervisorId,
        // request.getNote()));

        log.info("Procurement request rejected: {}", prId);

        return createApprovalResponse(approval.getId(), pr, ApprovalDecision.REJECT);
    }

    /**
     * Return procurement request to operator for revision
     */
    @Transactional
    public ApprovalResponseDTO returnProcurementRequest(UUID prId, ApprovalRequestDTO request, UUID supervisorId) {
        log.info("Returning procurement request: {} by supervisor: {}", prId, supervisorId);

        if (request.getDecision() != ApprovalDecision.RETURN) {
            throw new ValidationException("This endpoint only handles returns");
        }

        if (request.getNote() == null || request.getNote().isBlank()) {
            throw new ValidationException("Return reason is required");
        }

        ProcurementRequest pr = getProcurementRequestForApproval(prId);

        // Create approval record
        ApprovalHistory approval = createApprovalRecord(pr, supervisorId, ApprovalRole.SUPERVISOR, request);
        approvalHistoryRepository.save(approval);

        // Update PR status
        pr.setStatus(ProcurementStatus.RETURNED);
        pr.setUpdatedBy(supervisorId);
        procurementRequestRepository.save(pr);

        // Publish PR returned event for notification
        // TODO: Publish event

        log.info("Procurement request returned for revision: {}", prId);

        return createApprovalResponse(approval.getId(), pr, ApprovalDecision.RETURN);
    }

    /**
     * Add feedback/comment without changing status
     */
    @Transactional
    public ApprovalResponseDTO addFeedback(UUID prId, ApprovalRequestDTO request, UUID supervisorId) {
        log.info("Adding feedback to procurement request: {} by supervisor: {}", prId, supervisorId);

        if (request.getDecision() != ApprovalDecision.FEEDBACK_ONLY) {
            throw new ValidationException("This endpoint only handles feedback");
        }

        if (request.getNote() == null || request.getNote().isBlank()) {
            throw new ValidationException("Feedback note is required");
        }

        ProcurementRequest pr = procurementRequestRepository.findById(prId)
                .orElseThrow(() -> new ResourceNotFoundException("Procurement Request not found: " + prId));

        // Feedback can be added at any status (except rejected/cancelled)
        if (pr.getStatus() == ProcurementStatus.REJECTED || pr.getStatus() == ProcurementStatus.CANCELLED) {
            throw new ValidationException("Cannot add feedback to rejected or cancelled PR");
        }

        // Create approval record (feedback only, no status change)
        ApprovalHistory approval = createApprovalRecord(pr, supervisorId, ApprovalRole.SUPERVISOR, request);
        approvalHistoryRepository.save(approval);

        log.info("Feedback added to procurement request: {}", prId);

        return createApprovalResponse(approval.getId(), pr, ApprovalDecision.FEEDBACK_ONLY);
    }

    // Helper methods

    private ProcurementRequest getProcurementRequestForApproval(UUID prId) {
        ProcurementRequest pr = procurementRequestRepository.findById(prId)
                .orElseThrow(() -> new ResourceNotFoundException("Procurement Request not found: " + prId));

        // Only submitted or escalated PRs can be approved/rejected/returned
        if (pr.getStatus() != ProcurementStatus.SUBMITTED && pr.getStatus() != ProcurementStatus.ESCALATED) {
            throw new ValidationException("Only submitted or escalated procurement requests can be reviewed");
        }

        return pr;
    }

    private ApprovalHistory createApprovalRecord(ProcurementRequest pr, UUID approverId,
            ApprovalRole role, ApprovalRequestDTO request) {
        return ApprovalHistory.builder()
                .procurementRequest(pr)
                .approverId(approverId)
                .role(role)
                .decision(request.getDecision())
                .note(request.getNote())
                .timestamp(LocalDateTime.now())
                .build();
    }

    private ApprovalResponseDTO createApprovalResponse(UUID approvalId, ProcurementRequest pr,
            ApprovalDecision decision) {
        return ApprovalResponseDTO.builder()
                .approvalId(approvalId)
                .procurementRequestId(pr.getId())
                .decision(decision)
                .approverId(null) // Will be set from request context
                .approvedAt(LocalDateTime.now())
                .newStatus(pr.getStatus().name())
                .build();
    }

    private PendingApprovalDTO mapToPendingApprovalDTO(ProcurementRequest pr) {
        long daysWaiting = java.time.Duration.between(pr.getCreatedAt(), LocalDateTime.now()).toDays();

        return PendingApprovalDTO.builder()
                .procurementRequestId(pr.getId())
                .description(pr.getDescription())
                .type(pr.getType().name())
                .priority(pr.getPriority().name())
                .submittedAt(pr.getCreatedAt())
                .deadline(pr.getDeadline())
                .daysWaiting((int) daysWaiting)
                .build();
    }
}
