package com.tugasakhir.vendor_service.repository.delivery;

import com.tugasakhir.vendor_service.model.delivery.PackingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackingListRepository extends JpaRepository<PackingList, String> {
}
