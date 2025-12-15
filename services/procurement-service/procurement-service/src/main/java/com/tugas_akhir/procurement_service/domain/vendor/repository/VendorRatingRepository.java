package com.tugas_akhir.procurement_service.domain.vendor.repository;

import com.tugas_akhir.procurement_service.domain.vendor.entity.VendorRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VendorRatingRepository extends JpaRepository<VendorRating, UUID> {

    List<VendorRating> findByVendorId(UUID vendorId);

    @Query("SELECT AVG(vr.rating) FROM VendorRating vr WHERE vr.vendorId = :vendorId")
    Double getAverageRating(UUID vendorId);
}
