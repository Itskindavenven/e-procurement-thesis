package com.tugas_akhir.admin_service.masterdata.entity;

import com.tugas_akhir.admin_service.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "master_data_items")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class MasterDataItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "item_id")
    private UUID itemId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 50, unique = true)
    private String code;

    @Column(length = 50)
    private String category;

    @Column(length = 20)
    private String unit;

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";
}
