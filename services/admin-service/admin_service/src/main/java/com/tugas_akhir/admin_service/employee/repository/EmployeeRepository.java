package com.tugas_akhir.admin_service.employee.repository;

import com.tugas_akhir.admin_service.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);
}
