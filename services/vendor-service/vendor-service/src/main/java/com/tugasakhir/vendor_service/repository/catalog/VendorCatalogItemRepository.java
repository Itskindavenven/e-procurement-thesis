package com.tugasakhir.vendor_service.repository.catalog;

import com.tugasakhir.vendor_service.model.catalog.VendorCatalogItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorCatalogItemRepository extends JpaRepository<VendorCatalogItem, String> {
    List<VendorCatalogItem> findByVendorId(String vendorId);
}
