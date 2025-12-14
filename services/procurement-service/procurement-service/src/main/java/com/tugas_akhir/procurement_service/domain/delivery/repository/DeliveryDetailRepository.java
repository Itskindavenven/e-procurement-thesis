package com.tugas_akhir.procurement_service.domain.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tugas_akhir.procurement_service.domain.delivery.entity.DeliveryDetail;

import java.util.UUID;

@Repository
public interface DeliveryDetailRepository extends JpaRepository<DeliveryDetail, UUID> {
}

