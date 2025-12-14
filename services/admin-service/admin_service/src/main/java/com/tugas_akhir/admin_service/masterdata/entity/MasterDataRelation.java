package com.tugas_akhir.admin_service.masterdata.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "master_data_relations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MasterDataRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "relation_id")
    private UUID relationId;

    @Column(name = "parent_id", nullable = false)
    private UUID parentId;

    @Column(name = "child_id", nullable = false)
    private UUID childId;

    @Column(name = "relation_type", nullable = false, length = 50)
    private String relationType; // e.g., CATEGORY_ITEM

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "created_by", length = 50)
    private String createdBy;
}
