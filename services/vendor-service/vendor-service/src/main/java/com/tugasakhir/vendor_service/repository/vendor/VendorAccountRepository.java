package com.tugasakhir.vendor_service.repository.vendor;

import com.tugasakhir.vendor_service.model.vendor.VendorAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorAccountRepository extends JpaRepository<VendorAccount, String> {
}
