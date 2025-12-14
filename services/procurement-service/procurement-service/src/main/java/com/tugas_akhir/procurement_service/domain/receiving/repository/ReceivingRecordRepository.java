package com.tugas_akhir.procurement_service.domain.receiving.repository;

import com.tugas_akhir.procurement_service.common.enums.ReceivingStatus;
import com.tugas_akhir.procurement_service.domain.receiving.entity.ReceivingRecord;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for ReceivingRecord operations
 */
@Repository
public interface ReceivingRecordRepository extends JpaRepository<ReceivingRecord, UUID> {

    /**
     * Find all receiving records for a PO
     */
    List<ReceivingRecord> findByPoHeaderId(UUID poId);

    /**
     * Find receiving records by status
     */
    Page<ReceivingRecord> findByStatus(ReceivingStatus status, Pageable pageable);

    /**
     * Find receiving records by operator
     */
    Page<ReceivingRecord> findByReceivedBy(UUID operatorId, Pageable pageable);

    /**
     * Find latest receiving for a PO
     */
    @Query("SELECT r FROM ReceivingRecord r WHERE r.poHeader.id = :poId ORDER BY r.receivedAt DESC")
    Optional<ReceivingRecord> findLatestByPoId(@Param("poId") UUID poId);

    /**
     * Count receiving records by status
     */
    long countByStatus(ReceivingStatus status);
}
