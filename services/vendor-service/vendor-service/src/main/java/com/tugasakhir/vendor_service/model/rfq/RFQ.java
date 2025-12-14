package com.tugasakhir.vendor_service.model.rfq;

import com.tugasakhir.vendor_service.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rfqs")
public class RFQ extends BaseEntity {

    @Id
    private String rfqId;

    private String prId;
    private String vendorId;
    private String tipe; // OPEN, INVITE_ONLY
    private String statusRfq; // OPEN, RESPONDED, CLOSED, EXPIRED
    private LocalDateTime tenggatRespon;
}
