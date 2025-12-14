package com.tugasakhir.vendor_service.repository.rfq;

import com.tugasakhir.vendor_service.model.rfq.RFQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RFQRepository extends JpaRepository<RFQ, String> {
    List<RFQ> findByVendorId(String vendorId);

    List<RFQ> findByStatusRfq(String statusRfq);
}
