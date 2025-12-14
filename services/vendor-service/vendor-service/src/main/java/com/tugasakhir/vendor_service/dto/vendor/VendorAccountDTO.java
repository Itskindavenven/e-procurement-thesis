package com.tugasakhir.vendor_service.dto.vendor;

import lombok.Data;

@Data
public class VendorAccountDTO {
    private String vendorId;
    private String namaPerusahaan;
    private String emailKontak;
    private String status;
    private Double ratingPerforma;
}
