package com.tugas_akhir.procurement_service.domain.audit.repository;

import com.tugas_akhir.procurement_service.domain.audit.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByEntityIdOrderByTimestampDesc(UUID entityId);

    List<AuditLog> findByPerformedByOrderByTimestampDesc(UUID performedBy);
}
