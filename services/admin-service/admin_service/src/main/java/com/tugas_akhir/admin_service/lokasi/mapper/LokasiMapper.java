package com.tugas_akhir.admin_service.lokasi.mapper;

import com.tugas_akhir.admin_service.lokasi.dto.LokasiDTO;
import com.tugas_akhir.admin_service.lokasi.entity.Lokasi;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LokasiMapper {

    LokasiDTO toDTO(Lokasi lokasi);

    @Mapping(target = "locationId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Lokasi toEntity(LokasiDTO dto);

    @Mapping(target = "locationId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromDTO(LokasiDTO dto, @MappingTarget Lokasi lokasi);
}
