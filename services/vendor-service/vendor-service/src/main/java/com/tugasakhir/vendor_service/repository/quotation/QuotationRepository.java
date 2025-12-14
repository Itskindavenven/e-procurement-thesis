package com.tugasakhir.vendor_service.repository.quotation;

import com.tugasakhir.vendor_service.model.quotation.QuotationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuotationRepository extends JpaRepository<QuotationResponse, String> {
    List<QuotationResponse> findByRfqId(String rfqId);

    Optional<QuotationResponse> findByRfqIdAndVendorId(String rfqId, String vendorId);
}
