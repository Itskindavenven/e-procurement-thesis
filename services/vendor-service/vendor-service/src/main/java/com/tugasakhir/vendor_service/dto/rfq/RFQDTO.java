package com.tugasakhir.vendor_service.dto.rfq;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RFQDTO {
    private String rfqId;
    private String prId;
    private String vendorId;
    private String tipe;
    private String statusRfq;
    private LocalDateTime tenggatRespon;
}
