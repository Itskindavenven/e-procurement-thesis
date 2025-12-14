package com.tugas_akhir.admin_service.audit.repository;

import com.tugas_akhir.admin_service.audit.entity.LogSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LogSystemRepository extends JpaRepository<LogSystem, UUID> {
    List<LogSystem> findByServiceName(String serviceName);

    List<LogSystem> findByUserId(String userId);
}
