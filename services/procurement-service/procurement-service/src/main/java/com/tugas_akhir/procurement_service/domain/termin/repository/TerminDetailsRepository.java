package com.tugas_akhir.procurement_service.domain.termin.repository;

import com.tugas_akhir.procurement_service.common.enums.TerminStatus;
import com.tugas_akhir.procurement_service.domain.termin.entity.TerminDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository for TerminDetails operations
 */
@Repository
public interface TerminDetailsRepository extends JpaRepository<TerminDetails, UUID> {

    /**
     * Find termins by status (for operator pending review)
     */
    Page<TerminDetails> findByStatus(TerminStatus status, Pageable pageable);

    /**
     * Find termins by procurement request
     */
    List<TerminDetails> findByProcurementRequestId(UUID procurementRequestId);

    /**
     * Find pending termins for operator review
     */
    @Query("SELECT t FROM TerminDetails t WHERE t.status = :status ORDER BY t.vendorSubmittedAt ASC")
    Page<TerminDetails> findPendingForReview(@Param("status") TerminStatus status, Pageable pageable);

    /**
     * Count termins by status
     */
    long countByStatus(TerminStatus status);

    /**
     * Find overdue termins (submitted but not reviewed within X days)
     */
    @Query("SELECT t FROM TerminDetails t WHERE t.status = :status AND t.vendorSubmittedAt < :beforeDate")
    List<TerminDetails> findOverdueTermins(
            @Param("status") TerminStatus status,
            @Param("beforeDate") LocalDateTime beforeDate);
}
