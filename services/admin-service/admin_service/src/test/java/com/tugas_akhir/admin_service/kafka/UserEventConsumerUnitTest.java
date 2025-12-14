package com.tugas_akhir.admin_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tugas_akhir.admin_service.dto.kafka.UserEventDto;
import com.tugas_akhir.admin_service.vendor.dto.VendorDTO;
import com.tugas_akhir.admin_service.vendor.repository.VendorRepository;
import com.tugas_akhir.admin_service.vendor.service.VendorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEventConsumerUnitTest {

    @Mock
    private VendorService vendorService;

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private UserEventConsumer consumer;

    @Test
    void listen_UserCreated_VendorRole_CreatesRegistration() throws Exception {
        String json = "{\"eventType\":\"USER_CREATED\"}";
        UserEventDto dto = new UserEventDto();
        dto.setEventType("USER_CREATED");
        dto.setUserId(UUID.randomUUID().toString());

        UserEventDto.RoleDto role = new UserEventDto.RoleDto();
        role.setName("ROLE_VENDOR");
        dto.setRoles(List.of(role));

        when(objectMapper.readValue(json, UserEventDto.class)).thenReturn(dto);
        when(vendorRepository.existsByVendorId(any(UUID.class))).thenReturn(false);

        consumer.listen(json);

        verify(vendorService).createVendorRegistration(any(VendorDTO.class));
    }

    @Test
    void listen_UserCreated_NoVendorRole_Ignores() throws Exception {
        String json = "{\"eventType\":\"USER_CREATED\"}";
        UserEventDto dto = new UserEventDto();
        dto.setEventType("USER_CREATED");
        dto.setRoles(List.of());

        when(objectMapper.readValue(json, UserEventDto.class)).thenReturn(dto);

        consumer.listen(json);

        verify(vendorService, never()).createVendorRegistration(any());
    }

    @Test
    void listen_UserCreated_AlreadyExists_Ignores() throws Exception {
        String json = "{\"eventType\":\"USER_CREATED\"}";
        UserEventDto dto = new UserEventDto();
        dto.setEventType("USER_CREATED");
        dto.setUserId(UUID.randomUUID().toString());

        UserEventDto.RoleDto role = new UserEventDto.RoleDto();
        role.setName("ROLE_VENDOR");
        dto.setRoles(List.of(role));

        when(objectMapper.readValue(json, UserEventDto.class)).thenReturn(dto);
        when(vendorRepository.existsByVendorId(any(UUID.class))).thenReturn(true);

        consumer.listen(json);

        verify(vendorService, never()).createVendorRegistration(any());
    }
}
