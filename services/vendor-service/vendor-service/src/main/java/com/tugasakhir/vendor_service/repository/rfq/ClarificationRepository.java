package com.tugasakhir.vendor_service.repository.rfq;

import com.tugasakhir.vendor_service.model.rfq.Clarification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClarificationRepository extends JpaRepository<Clarification, String> {
    List<Clarification> findByRfqId(String rfqId);

    List<Clarification> findByVendorId(String vendorId);
}
