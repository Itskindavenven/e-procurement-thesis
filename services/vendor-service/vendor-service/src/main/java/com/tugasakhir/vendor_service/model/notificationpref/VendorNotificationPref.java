package com.tugasakhir.vendor_service.model.notificationpref;

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
@Table(name = "vendor_notification_prefs")
public class VendorNotificationPref extends BaseEntity {

    @Id
    private String vendorId;

    private Boolean emailOn;
    private Boolean pushOn;
    private String kategori; // JSON or CSV
}
