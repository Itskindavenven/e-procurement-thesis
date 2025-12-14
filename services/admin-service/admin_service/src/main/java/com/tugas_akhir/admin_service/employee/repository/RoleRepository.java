package com.tugas_akhir.admin_service.employee.repository;

import com.tugas_akhir.admin_service.employee.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
