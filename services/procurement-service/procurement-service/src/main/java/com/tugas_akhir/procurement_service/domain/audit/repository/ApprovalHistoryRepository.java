package com.tugas_akhir.procurement_service.domain.audit.repository;

import com.tugas_akhir.procurement_service.domain.audit.entity.ApprovalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ApprovalHistoryRepository extends JpaRepository<ApprovalHistory, UUID> {
}

