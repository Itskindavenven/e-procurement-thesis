package com.tugas_akhir.procurement_service.domain.serviceProgress.repository;

import com.tugas_akhir.procurement_service.domain.serviceProgress.entity.ServiceProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceProgressRepository extends JpaRepository<ServiceProgress, UUID> {
}
