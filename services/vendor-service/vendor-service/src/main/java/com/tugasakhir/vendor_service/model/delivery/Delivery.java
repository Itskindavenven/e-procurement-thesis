package com.tugasakhir.vendor_service.model.delivery;

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
@Table(name = "deliveries")
public class Delivery extends BaseEntity {

    @Id
    private String deliveryId;

    private String poId;
    private String vendorId;
    private LocalDate deliveryDate; // Renamed from jadwalKirim for consistency if needed, or keep mapping. Let's
                                    // use deliveryDate.
    private String statusDelivery; // Renamed from statusKirim
    private String trackingNumber; // Renamed from logisticsTrackingNo
    private String logisticsProvider;

    @jakarta.persistence.OneToMany(mappedBy = "delivery", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private java.util.List<PackingList> packingLists = new java.util.ArrayList<>();
}
