package com.tugas_akhir.admin_service.procurement.repository;

import com.tugas_akhir.admin_service.procurement.entity.ProcurementCorrection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProcurementCorrectionRepository extends JpaRepository<ProcurementCorrection, UUID> {
    List<ProcurementCorrection> findByProcurementId(UUID procurementId);
}
