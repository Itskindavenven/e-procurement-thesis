package com.tugas_akhir.procurement_service.domain.procurementrequest.service;

import com.tugas_akhir.procurement_service.common.enums.ProcurementStatus;
import com.tugas_akhir.procurement_service.domain.procurementrequest.dto.ProcurementRequestDTOs.*;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementItem;
import com.tugas_akhir.procurement_service.domain.procurementrequest.mapper.ProcurementRequestMapper;
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
 * Service for managing Procurement Requests
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProcurementRequestService {

    private final ProcurementRequestRepository procurementRequestRepository;
    private final ProcurementRequestMapper mapper;
    private final ProcurementEventProducer eventProducer;

    // TODO: Add when available
    // private final VendorServiceClient vendorServiceClient;

    /**
     * Create a new procurement request (draft)
     */
    @Transactional
    public ProcurementRequestResponseDTO createProcurementRequest(CreateProcurementRequestDTO dto, UUID operatorId) {
        log.info("Creating procurement request for operator: {}", operatorId);

        // TODO: Validate vendor exists and is active
        // vendorServiceClient.isVendorActive(dto.getVendorId());

        // Map DTO to entity
        ProcurementRequest pr = mapper.toEntity(dto);
        pr.setOperatorId(operatorId);
        pr.setStatus(ProcurementStatus.DRAFT);
        pr.setCreatedBy(operatorId);

        // Set bidirectional relationships for items
        if (pr.getItems() != null) {
            for (ProcurementItem item : pr.getItems()) {
                item.setProcurementRequest(pr);
            }
        }

        // Set bidirectional relationships for delivery details
        if (pr.getDeliveryDetails() != null) {
            pr.getDeliveryDetails().forEach(dd -> dd.setProcurementRequest(pr));
        }

        // Set bidirectional relationships for documents
        if (pr.getDocuments() != null) {
            pr.getDocuments().forEach(doc -> doc.setProcurementRequest(pr));
        }

        ProcurementRequest saved = procurementRequestRepository.save(pr);

        log.info("Procurement request created successfully: {}", saved.getId());

        return mapper.toResponseDTO(saved);
    }

    /**
     * Update a draft procurement request
     */
    @Transactional
    public ProcurementRequestResponseDTO updateProcurementRequest(UUID prId, UpdateProcurementRequestDTO dto,
            UUID operatorId) {
        log.info("Updating procurement request: {}", prId);

        ProcurementRequest pr = procurementRequestRepository.findById(prId)
                .orElseThrow(() -> new ResourceNotFoundException("Procurement Request not found: " + prId));

        // Validate ownership
        if (!pr.getOperatorId().equals(operatorId)) {
            throw new ValidationException("You are not authorized to update this procurement request");
        }

        // Only drafts or returned PRs can be updated
        if (pr.getStatus() != ProcurementStatus.DRAFT && pr.getStatus() != ProcurementStatus.RETURNED) {
            throw new ValidationException("Only draft or returned procurement requests can be updated");
        }

        // Use mapper to update entity from DTO (null values ignored)
        mapper.updateEntityFromDTO(dto, pr);
        pr.setUpdatedBy(operatorId);

        // TODO: Handle items, delivery details, documents updates separately if
        // provided

        ProcurementRequest updated = procurementRequestRepository.save(pr);

        log.info("Procurement request updated successfully: {}", prId);

        return mapper.toResponseDTO(updated);
    }

    /**
     * Submit procurement request for approval
     */
    @Transactional
    public ProcurementRequestResponseDTO submitForApproval(UUID prId, UUID operatorId) {
        log.info("Submitting procurement request for approval: {}", prId);

        ProcurementRequest pr = procurementRequestRepository.findById(prId)
                .orElseThrow(() -> new ResourceNotFoundException("Procurement Request not found: " + prId));

        // Validate ownership
        if (!pr.getOperatorId().equals(operatorId)) {
            throw new ValidationException("You are not authorized to submit this procurement request");
        }

        // Only drafts or returned PRs can be submitted
        if (pr.getStatus() != ProcurementStatus.DRAFT && pr.getStatus() != ProcurementStatus.RETURNED) {
            throw new ValidationException("Only draft or returned procurement requests can be submitted");
        }

        // Validate PR is complete
        if (pr.getItems() == null || pr.getItems().isEmpty()) {
            throw new ValidationException("Cannot submit PR without items");
        }

        pr.setStatus(ProcurementStatus.SUBMITTED);
        pr.setUpdatedBy(operatorId);

        ProcurementRequest submitted = procurementRequestRepository.save(pr);

        // Publish PR submitted event
        eventProducer.publishPRSubmitted(createPRSubmittedEvent(submitted, operatorId));

        log.info("Procurement request submitted successfully: {}", prId);

        return mapper.toResponseDTO(submitted);
    }

    /**
     * Get procurement request by ID
     */
    @Transactional(readOnly = true)
    public ProcurementRequestResponseDTO getProcurementRequestById(UUID prId) {
        log.info("Fetching procurement request: {}", prId);

        ProcurementRequest pr = procurementRequestRepository.findById(prId)
                .orElseThrow(() -> new ResourceNotFoundException("Procurement Request not found: " + prId));

        return mapper.toResponseDTO(pr);
    }

    /**
     * List procurement requests for operator
     */
    @Transactional(readOnly = true)
    public Page<ProcurementRequestSummaryDTO> listProcurementRequests(UUID operatorId, ProcurementStatus status,
            Pageable pageable) {
        log.info("Listing procurement requests for operator: {}, status: {}", operatorId, status);

        Page<ProcurementRequest> prs;

        if (status != null) {
            prs = procurementRequestRepository.findByOperatorIdAndStatus(operatorId, status, pageable);
        } else {
            prs = procurementRequestRepository.findByOperatorId(operatorId, pageable);
        }

        return prs.map(mapper::toSummaryDTO);
    }

    /**
     * Soft delete procurement request (only drafts)
     */
    @Transactional
    public void deleteProcurementRequest(UUID prId, UUID operatorId) {
        log.info("Deleting procurement request: {}", prId);

        ProcurementRequest pr = procurementRequestRepository.findById(prId)
                .orElseThrow(() -> new ResourceNotFoundException("Procurement Request not found: " + prId));

        // Validate ownership
        if (!pr.getOperatorId().equals(operatorId)) {
            throw new ValidationException("You are not authorized to delete this procurement request");
        }

        // Only drafts can be deleted
        if (pr.getStatus() != ProcurementStatus.DRAFT) {
            throw new ValidationException("Only draft procurement requests can be deleted");
        }

        pr.setIsDeleted(true);
        pr.setDeletedAt(LocalDateTime.now());
        pr.setUpdatedBy(operatorId);

        procurementRequestRepository.save(pr);

        log.info("Procurement request deleted successfully: {}", prId);
    }

    // ========== Event Creation Helper Methods ==========

    /**
     * Create PRSubmittedEvent from ProcurementRequest entity
     */
    private com.tugas_akhir.procurement_service.event.dto.ProcurementEvents.PRSubmittedEvent createPRSubmittedEvent(
            ProcurementRequest pr, UUID operatorId) {

        // Calculate total amount
        java.math.BigDecimal totalAmount = pr.getItems().stream()
                .map(item -> item.getUnitPrice()
                        .multiply(java.math.BigDecimal.valueOf(item.getQuantity()))
                        .add(item.getTaxPpn() != null ? item.getTaxPpn() : java.math.BigDecimal.ZERO))
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        return com.tugas_akhir.procurement_service.event.dto.ProcurementEvents.PRSubmittedEvent.builder()
                .procurementRequestId(pr.getId())
                .operatorId(operatorId)
                .vendorId(pr.getVendorId())
                .type(pr.getType().name())
                .description(pr.getDescription())
                .priority(pr.getPriority() != null ? pr.getPriority().name() : null)
                .totalAmount(totalAmount)
                .deadline(pr.getDeadline())
                .submittedAt(LocalDateTime.now())
                .locationId(pr.getLocationId())
                // TODO: Populate names from external services
                .operatorName(null)
                .vendorName(null)
                .build();
    }
}
