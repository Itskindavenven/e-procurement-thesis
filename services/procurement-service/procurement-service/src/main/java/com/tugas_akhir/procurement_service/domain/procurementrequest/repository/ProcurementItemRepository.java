package com.tugas_akhir.procurement_service.domain.procurementrequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementItem;

import java.util.UUID;

@Repository
public interface ProcurementItemRepository extends JpaRepository<ProcurementItem, UUID> {
}

