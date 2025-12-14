package com.tugasakhir.vendor_service.repository.delivery;

import com.tugasakhir.vendor_service.model.delivery.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, String> {
    List<String> findByVendorId(String vendorId);
}
