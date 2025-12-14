package com.tugasakhir.vendor_service.model.delivery;

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
@Table(name = "delivery_proofs")
public class DeliveryProof extends BaseEntity {

    @Id
    private String podId;

    private String deliveryId;
    private LocalDateTime waktuTerima;
    private String penerima;
    private String buktiFotoUrl;
}
