package com.tugasakhir.vendor_service.repository.invoice;

import com.tugasakhir.vendor_service.model.invoice.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    List<String> findByVendorId(String vendorId);
}
