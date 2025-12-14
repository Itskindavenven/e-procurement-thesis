package com.tugas_akhir.procurement_service.domain.procurementorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tugas_akhir.procurement_service.domain.procurementorder.entity.POItem;

import java.util.UUID;

@Repository
public interface POItemRepository extends JpaRepository<POItem, UUID> {
}

