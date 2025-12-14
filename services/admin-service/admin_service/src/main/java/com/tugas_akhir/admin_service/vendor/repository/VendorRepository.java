package com.tugas_akhir.admin_service.vendor.repository;

import com.tugas_akhir.admin_service.vendor.entity.VendorRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VendorRepository extends JpaRepository<VendorRegistration, UUID> {
    List<VendorRegistration> findByVerificationStatus(String status);

    Optional<VendorRegistration> findByVendorId(UUID vendorId);

    boolean existsByVendorId(UUID vendorId);
}
