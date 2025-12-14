package com.tugasakhir.vendor_service.dto.delivery;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class DeliveryDTO {
    private String deliveryId;
    private String poId;
    private String vendorId;
    private LocalDate deliveryDate;
    private String statusDelivery; // PENDING, SHIPPED, DELIVERED
    private String trackingNumber;
    private String logisticsProvider;
    private List<PackingListDTO> packingLists;
}
