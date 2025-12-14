package com.tugas_akhir.admin_service.masterdata.mapper;

import com.tugas_akhir.admin_service.masterdata.dto.MasterDataDTO;
import com.tugas_akhir.admin_service.masterdata.entity.MasterDataItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MasterDataMapper {

    MasterDataDTO toDTO(MasterDataItem item);

    @Mapping(target = "itemId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    MasterDataItem toEntity(MasterDataDTO dto);

    @Mapping(target = "itemId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromDTO(MasterDataDTO dto, @MappingTarget MasterDataItem item);
}
