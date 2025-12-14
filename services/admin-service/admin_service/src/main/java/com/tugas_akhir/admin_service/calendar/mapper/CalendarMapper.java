package com.tugas_akhir.admin_service.calendar.mapper;

import com.tugas_akhir.admin_service.calendar.dto.CalendarDTO;
import com.tugas_akhir.admin_service.calendar.dto.NonActiveDayDTO;
import com.tugas_akhir.admin_service.calendar.entity.Calendar;
import com.tugas_akhir.admin_service.calendar.entity.NonActiveDay;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CalendarMapper {

    CalendarDTO toDTO(Calendar calendar);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Calendar toEntity(CalendarDTO dto);

    NonActiveDayDTO toDTO(NonActiveDay nonActiveDay);

    @Mapping(target = "nonActiveId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    NonActiveDay toEntity(NonActiveDayDTO dto);
}
