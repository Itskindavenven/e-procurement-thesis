package com.tugasakhir.vendor_service.dto.quotation;

import com.tugasakhir.vendor_service.model.quotation.QuotationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuotationMapper {
    QuotationDTO toDto(QuotationResponse entity);

    QuotationResponse toEntity(QuotationDTO dto);
}
