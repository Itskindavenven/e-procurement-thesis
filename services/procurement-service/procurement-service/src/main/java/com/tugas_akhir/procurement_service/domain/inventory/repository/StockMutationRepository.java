package com.tugas_akhir.procurement_service.domain.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tugas_akhir.procurement_service.domain.inventory.entity.StockMutation;

import java.util.UUID;

@Repository
public interface StockMutationRepository extends JpaRepository<StockMutation, UUID> {
}

