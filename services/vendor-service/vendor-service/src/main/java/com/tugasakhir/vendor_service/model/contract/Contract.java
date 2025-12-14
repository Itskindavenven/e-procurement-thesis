package com.tugasakhir.vendor_service.model.contract;

import com.tugasakhir.vendor_service.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contracts")
public class Contract extends BaseEntity {

    @Id
    private String contractId;

    private String poId;
    private String soId; // Service Order ID
    private String vendorId;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;
    private String ketentuan;
    private String status; // ACTIVE, EXPIRED, TERMINATED
}
