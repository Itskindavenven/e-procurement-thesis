package com.tugas_akhir.admin_service.employee.mapper;

import com.tugas_akhir.admin_service.employee.dto.EmployeeDTO;
import com.tugas_akhir.admin_service.employee.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(source = "role.roleId", target = "roleId")
    @Mapping(source = "role.roleName", target = "roleName")
    EmployeeDTO toDTO(Employee employee);

    @Mapping(target = "role", ignore = true) // Role is set manually in service
    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Employee toEntity(EmployeeDTO dto);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromDTO(EmployeeDTO dto, @MappingTarget Employee employee);
}
