package com.tugas_akhir.admin_service.lokasi.repository;

import com.tugas_akhir.admin_service.lokasi.entity.Lokasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LokasiRepository extends JpaRepository<Lokasi, UUID> {
    boolean existsByLocationName(String locationName);
}
