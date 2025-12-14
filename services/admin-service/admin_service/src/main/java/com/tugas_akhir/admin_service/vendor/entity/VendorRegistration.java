package com.tugas_akhir.admin_service.vendor.entity;

import com.tugas_akhir.admin_service.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "vendor_registrations")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class VendorRegistration extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "registration_id")
    private UUID registrationId;

    @Column(name = "vendor_id", nullable = false)
    private UUID vendorId;

    @Column(name = "document_url", columnDefinition = "TEXT")
    private String documentUrl;

    @Column(name = "verification_status", nullable = false, length = 20)
    private String verificationStatus = "PENDING"; // PENDING, APPROVED, REJECTED, REVISION_REQUESTED

    @Column(name = "admin_notes", columnDefinition = "TEXT")
    private String adminNotes;
}
