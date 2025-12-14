package com.tugas_akhir.auth_service.repository;

import com.tugas_akhir.auth_service.domain.PasswordResetRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetRequestRepository extends JpaRepository<PasswordResetRequest, UUID> {
    Optional<PasswordResetRequest> findByToken(String token);
}
