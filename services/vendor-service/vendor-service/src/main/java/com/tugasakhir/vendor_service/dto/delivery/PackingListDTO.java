package com.tugasakhir.vendor_service.dto.delivery;

import lombok.Data;

@Data
public class PackingListDTO {
    private String packingListId;
    private String itemId;
    private Integer quantity;
    private String boxNumber;
}
