package com.tugas_akhir.admin_service.audit.mapper;

import com.tugas_akhir.admin_service.audit.dto.LogSystemDTO;
import com.tugas_akhir.admin_service.audit.entity.LogSystem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LogSystemMapper {

    LogSystemDTO toDTO(LogSystem logSystem);

    @Mapping(target = "logId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    LogSystem toEntity(LogSystemDTO dto);
}
