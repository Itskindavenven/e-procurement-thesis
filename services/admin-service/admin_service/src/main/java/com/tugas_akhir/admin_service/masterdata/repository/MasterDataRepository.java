package com.tugas_akhir.admin_service.masterdata.repository;

import com.tugas_akhir.admin_service.masterdata.entity.MasterDataItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MasterDataRepository extends JpaRepository<MasterDataItem, UUID> {
    Optional<MasterDataItem> findByCode(String code);

    boolean existsByCode(String code);
}
