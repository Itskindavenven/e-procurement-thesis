package com.tugas_akhir.admin_service.notification.repository;

import com.tugas_akhir.admin_service.notification.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, UUID> {
    Optional<NotificationTemplate> findByTemplateCode(String templateCode);

    boolean existsByTemplateCode(String templateCode);
}
