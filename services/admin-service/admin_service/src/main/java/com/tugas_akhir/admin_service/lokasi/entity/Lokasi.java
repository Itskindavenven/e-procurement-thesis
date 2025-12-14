package com.tugas_akhir.admin_service.lokasi.entity;

import com.tugas_akhir.admin_service.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "locations")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Lokasi extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "location_id")
    private UUID locationId;

    @Column(name = "location_name", nullable = false, length = 100)
    private String locationName;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "location_type", nullable = false, length = 50)
    private String locationType; // HEAD_OFFICE, BRANCH, WAREHOUSE, PROJECT_SITE

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";
}
