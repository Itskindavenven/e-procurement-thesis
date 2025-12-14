package com.tugas_akhir.admin_service.notification.repository;

import com.tugas_akhir.admin_service.notification.entity.NotificationRecipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRecipientRepository extends JpaRepository<NotificationRecipient, UUID> {
    List<NotificationRecipient> findByLogId(UUID logId);
}
