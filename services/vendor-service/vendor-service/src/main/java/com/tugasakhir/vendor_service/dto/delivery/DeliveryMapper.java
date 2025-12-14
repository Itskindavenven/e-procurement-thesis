package com.tugasakhir.vendor_service.dto.delivery;

import com.tugasakhir.vendor_service.model.delivery.Delivery;
import com.tugasakhir.vendor_service.model.delivery.PackingList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    DeliveryDTO toDto(Delivery entity);

    Delivery toEntity(DeliveryDTO dto);

    PackingListDTO toDto(PackingList entity);

    PackingList toEntity(PackingListDTO dto);
}
