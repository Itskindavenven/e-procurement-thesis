package com.tugas_akhir.procurement_service.domain.procurementrequest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tugas_akhir.procurement_service.common.enums.ProcurementStatus;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository for Procurement Request operations
 */
@Repository
public interface ProcurementRequestRepository extends JpaRepository<ProcurementRequest, UUID> {

    /**
     * Find all PRs for a specific operator
     */
    Page<ProcurementRequest> findByOperatorId(UUID operatorId, Pageable pageable);

    /**
     * Find PRs by operator and status
     */
    Page<ProcurementRequest> findByOperatorIdAndStatus(UUID operatorId, ProcurementStatus status, Pageable pageable);

    /**
     * Find PRs by status (for supervisor pending approvals)
     */
    Page<ProcurementRequest> findByStatus(ProcurementStatus status, Pageable pageable);

    /**
     * Count PRs by operator and status
     */
    long countByOperatorIdAndStatus(UUID operatorId, ProcurementStatus status);

    /**
     * Count PRs by status
     */
    long countByStatus(ProcurementStatus status);

    /**
     * Find submitted PRs older than specified time (for auto-reminder and
     * auto-escalation)
     */
    @Query(value = "SELECT pr FROM ProcurementRequest pr WHERE pr.status = :status AND pr.createdAt < :beforeTime")
    List<ProcurementRequest> findByStatusAndCreatedAtBefore(
            @Param("status") ProcurementStatus status,
            @Param("beforeTime") LocalDateTime beforeTime);

    /**
     * Find PRs by multiple statuses (for dashboard)
     */
    @Query("SELECT pr FROM ProcurementRequest pr WHERE pr.status IN :statuses")
    List<ProcurementRequest> findByStatusIn(@Param("statuses") List<ProcurementStatus> statuses);

    /**
     * Find PRs by operator and vendor
     */
    List<ProcurementRequest> findByOperatorIdAndVendorId(UUID operatorId, UUID vendorId);

    // ========== Dashboard Query Methods ==========

    /**
     * Count all PRs for an operator
     */
    long countByOperatorId(UUID operatorId);

    /**
     * Count PRs created after a specific date
     */
    long countByOperatorIdAndCreatedAtAfter(UUID operatorId, LocalDateTime createdAt);

    /**
     * Count PRs created between two dates
     */
    long countByOperatorIdAndCreatedAtBetween(UUID operatorId, LocalDateTime start, LocalDateTime end);

    /**
     * Count PRs by operator, status, and updated between dates
     */
    long countByOperatorIdAndStatusAndUpdatedAtBetween(UUID operatorId, ProcurementStatus status,
            LocalDateTime start, LocalDateTime end);
}
