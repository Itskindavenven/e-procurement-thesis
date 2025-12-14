package com.tugas_akhir.admin_service.procurement.repository;

import com.tugas_akhir.admin_service.procurement.entity.AuditInvestigationFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditInvestigationFlagRepository extends JpaRepository<AuditInvestigationFlag, UUID> {
    List<AuditInvestigationFlag> findByProcurementId(UUID procurementId);

    List<AuditInvestigationFlag> findByStatus(String status);
}
