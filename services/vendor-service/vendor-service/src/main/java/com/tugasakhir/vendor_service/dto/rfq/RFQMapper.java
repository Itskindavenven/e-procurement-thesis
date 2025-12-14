package com.tugasakhir.vendor_service.dto.rfq;

import com.tugasakhir.vendor_service.model.rfq.RFQ;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RFQMapper {
    RFQDTO toDto(RFQ entity);

    RFQ toEntity(RFQDTO dto);
}
