package com.tugasakhir.vendor_service.model.delivery;

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
@Table(name = "packing_lists")
public class PackingList extends BaseEntity {

    @Id
    private String packingListId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    private String itemId;
    private Integer quantity;
    private String boxNumber;
}
