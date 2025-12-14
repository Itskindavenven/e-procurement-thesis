package com.tugas_akhir.admin_service.notification.mapper;

import com.tugas_akhir.admin_service.notification.dto.NotificationTemplateDTO;
import com.tugas_akhir.admin_service.notification.entity.NotificationTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "placeholders", ignore = true) // Handled manually
    NotificationTemplateDTO toDTO(NotificationTemplate template);

    @Mapping(target = "templateId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    NotificationTemplate toEntity(NotificationTemplateDTO dto);

    @Mapping(target = "templateId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromDTO(NotificationTemplateDTO dto, @MappingTarget NotificationTemplate template);

    com.tugas_akhir.admin_service.notification.dto.NotificationLogDTO toDTO(
            com.tugas_akhir.admin_service.notification.entity.NotificationLog log);
}
