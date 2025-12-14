package com.tugas_akhir.admin_service.masterdata.repository;

import com.tugas_akhir.admin_service.masterdata.entity.MasterDataRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MasterDataRelationRepository extends JpaRepository<MasterDataRelation, UUID> {
    List<MasterDataRelation> findByParentId(UUID parentId);

    List<MasterDataRelation> findByChildId(UUID childId);
}
