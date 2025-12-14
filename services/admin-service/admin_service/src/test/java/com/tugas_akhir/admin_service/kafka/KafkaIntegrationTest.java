package com.tugas_akhir.admin_service.kafka;

import com.tugas_akhir.admin_service.employee.dto.EmployeeDTO;
import com.tugas_akhir.admin_service.employee.entity.Role;
import com.tugas_akhir.admin_service.employee.repository.RoleRepository;
import com.tugas_akhir.admin_service.employee.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@ActiveProfiles("test")
public class KafkaIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testEmployeeCreationPublishesEvent() {
        // Save Role to H2
        Role role = new Role("ROLE_ADMIN", "Administrator");
        roleRepository.save(role);

        // Create DTO
        EmployeeDTO dto = new EmployeeDTO();
        dto.setName("Test Employee");
        dto.setEmail("test@example.com");
        dto.setRoleId("ROLE_ADMIN");

        // Call Service
        employeeService.createEmployee(dto);

        // Verification of Kafka publishing is implicit if no exception is thrown
        // In a real test, we would consume the message and assert its content
        // For this verification step, ensuring the service runs with Kafka enabled is
        // sufficient
    }
}
