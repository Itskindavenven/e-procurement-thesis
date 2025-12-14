package com.tugasakhir.vendor_service.model.vendor;

import com.tugasakhir.vendor_service.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vendor_accounts")
public class VendorAccount extends BaseEntity {

    @Id
    private String vendorId;

    private String namaPerusahaan;
    private String emailKontak;
    private String status;
    private Double ratingPerforma;
}
