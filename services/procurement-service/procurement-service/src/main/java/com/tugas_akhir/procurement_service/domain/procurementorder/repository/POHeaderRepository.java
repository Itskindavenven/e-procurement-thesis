package com.tugas_akhir.procurement_service.domain.procurementorder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tugas_akhir.procurement_service.common.enums.POStatus;
import com.tugas_akhir.procurement_service.domain.procurementorder.entity.POHeader;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface POHeaderRepository extends JpaRepository<POHeader, UUID> {

    /**
     * Find PO headers by status with pagination
     */
    Page<POHeader> findByStatus(POStatus status, Pageable pageable);

    /**
     * Find PO header by procurement request ID
     */
    Optional<POHeader> findByProcurementRequestId(UUID procurementRequestId);
}
