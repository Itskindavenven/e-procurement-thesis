package com.tugasakhir.vendor_service.dto.catalog;

import com.tugasakhir.vendor_service.model.catalog.VendorCatalogItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CatalogMapper {
    VendorCatalogItemDTO toDto(VendorCatalogItem entity);

    VendorCatalogItem toEntity(VendorCatalogItemDTO dto);
}
