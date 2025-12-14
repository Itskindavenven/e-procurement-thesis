package com.tugas_akhir.admin_service.lokasi.repository;

import com.tugas_akhir.admin_service.lokasi.entity.SupervisorLocation;
import com.tugas_akhir.admin_service.lokasi.entity.SupervisorLocationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SupervisorLocationRepository extends JpaRepository<SupervisorLocation, SupervisorLocationId> {
    List<SupervisorLocation> findBySupervisorId(UUID supervisorId);

    List<SupervisorLocation> findByLocationId(UUID locationId);
}
