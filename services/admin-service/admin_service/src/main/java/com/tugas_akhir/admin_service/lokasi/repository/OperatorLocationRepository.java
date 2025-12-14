package com.tugas_akhir.admin_service.lokasi.repository;

import com.tugas_akhir.admin_service.lokasi.entity.OperatorLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OperatorLocationRepository extends JpaRepository<OperatorLocation, UUID> {
    Optional<OperatorLocation> findByOperatorId(UUID operatorId);

    boolean existsByOperatorId(UUID operatorId);
}
