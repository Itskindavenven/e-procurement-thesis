package com.tugas_akhir.admin_service.employee.service;

import com.tugas_akhir.admin_service.employee.dto.EmployeeDTO;
import com.tugas_akhir.admin_service.employee.entity.Employee;
import com.tugas_akhir.admin_service.employee.entity.Role;
import com.tugas_akhir.admin_service.employee.mapper.EmployeeMapper;
import com.tugas_akhir.admin_service.employee.repository.EmployeeRepository;
import com.tugas_akhir.admin_service.employee.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final EmployeeMapper employeeMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + id));
        return employeeMapper.toDTO(employee);
    }

    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO dto) {
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
        }

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + dto.getRoleId()));

        Employee employee = employeeMapper.toEntity(dto);
        employee.setRole(role);
        employee.setStatus("ACTIVE");

        Employee savedEmployee = employeeRepository.save(employee);

        // Publish event
        kafkaTemplate.send("admin.employee.events", "employee.created", savedEmployee.getEmployeeId().toString());

        return employeeMapper.toDTO(savedEmployee);
    }

    @Transactional
    public EmployeeDTO updateEmployee(UUID id, EmployeeDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + id));

        if (!employee.getEmail().equals(dto.getEmail()) && employeeRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
        }

        employeeMapper.updateEntityFromDTO(dto, employee);

        if (dto.getRoleId() != null) {
            Role role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + dto.getRoleId()));
            employee.setRole(role);
        }

        Employee updatedEmployee = employeeRepository.save(employee);

        // Publish event
        kafkaTemplate.send("admin.employee.events", "employee.updated", updatedEmployee.getEmployeeId().toString());

        return employeeMapper.toDTO(updatedEmployee);
    }

    @Transactional
    public void deactivateEmployee(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + id));

        employee.setStatus("INACTIVE");
        employee.setDeleted(true); // Soft delete
        employeeRepository.save(employee);

        // Publish event
        kafkaTemplate.send("admin.employee.events", "employee.deactivated", id.toString());
    }
}
