package com.tugas_akhir.admin_service.vendor.mapper;

import com.tugas_akhir.admin_service.vendor.dto.VendorDTO;
import com.tugas_akhir.admin_service.vendor.entity.VendorRegistration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VendorMapper {

    VendorDTO toDTO(VendorRegistration vendorRegistration);

    @Mapping(target = "registrationId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    VendorRegistration toEntity(VendorDTO dto);

    @Mapping(target = "registrationId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromDTO(VendorDTO dto, @MappingTarget VendorRegistration vendorRegistration);
}
