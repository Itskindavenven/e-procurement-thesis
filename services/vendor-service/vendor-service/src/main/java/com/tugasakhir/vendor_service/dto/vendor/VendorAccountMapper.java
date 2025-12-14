package com.tugasakhir.vendor_service.dto.vendor;

import com.tugasakhir.vendor_service.model.vendor.VendorAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VendorAccountMapper {
    VendorAccountDTO toDto(VendorAccount entity);

    VendorAccount toEntity(VendorAccountDTO dto);
}
