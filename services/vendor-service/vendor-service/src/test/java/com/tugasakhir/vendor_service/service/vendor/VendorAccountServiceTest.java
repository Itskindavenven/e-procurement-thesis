package com.tugasakhir.vendor_service.service.vendor;

import com.tugasakhir.vendor_service.dto.vendor.VendorAccountDTO;
import com.tugasakhir.vendor_service.dto.vendor.VendorAccountMapper;
import com.tugasakhir.vendor_service.exception.ResourceNotFoundException;
import com.tugasakhir.vendor_service.model.vendor.VendorAccount;
import com.tugasakhir.vendor_service.repository.vendor.VendorAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendorAccountServiceTest {

    @Mock
    private VendorAccountRepository repository;

    @Mock
    private VendorAccountMapper mapper;

    @InjectMocks
    private VendorAccountService service;

    private VendorAccount vendorAccount;
    private VendorAccountDTO vendorAccountDTO;

    @BeforeEach
    void setUp() {
        vendorAccount = new VendorAccount();
        vendorAccount.setVendorId(UUID.randomUUID().toString());
        vendorAccount.setNamaPerusahaan("Test Vendor");
        vendorAccount.setEmailKontak("test@vendor.com");

        vendorAccountDTO = new VendorAccountDTO();
        vendorAccountDTO.setVendorId(vendorAccount.getVendorId());
        vendorAccountDTO.setNamaPerusahaan(vendorAccount.getNamaPerusahaan());
        vendorAccountDTO.setEmailKontak(vendorAccount.getEmailKontak());
    }

    @Test
    void createVendor_Success() {
        when(mapper.toEntity(any(VendorAccountDTO.class))).thenReturn(vendorAccount);
        when(repository.save(any(VendorAccount.class))).thenReturn(vendorAccount);
        when(mapper.toDto(any(VendorAccount.class))).thenReturn(vendorAccountDTO);

        VendorAccountDTO result = service.createVendor(vendorAccountDTO);

        assertNotNull(result);
        assertEquals(vendorAccountDTO.getNamaPerusahaan(), result.getNamaPerusahaan());
        verify(repository, times(1)).save(any(VendorAccount.class));
    }

    @Test
    void getVendorById_Success() {
        when(repository.findById(vendorAccount.getVendorId())).thenReturn(Optional.of(vendorAccount));
        when(mapper.toDto(vendorAccount)).thenReturn(vendorAccountDTO);

        VendorAccountDTO result = service.getVendorById(vendorAccount.getVendorId());

        assertNotNull(result);
        assertEquals(vendorAccount.getVendorId(), result.getVendorId());
    }

    @Test
    void getVendorById_NotFound() {
        String id = "non-existent-id";
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getVendorById(id));
    }

    @Test
    void syncVendorFromUser_CreateNewVendor() {
        com.tugasakhir.vendor_service.dto.kafka.UserEventDto event = new com.tugasakhir.vendor_service.dto.kafka.UserEventDto();
        event.setUserId("user-123");
        event.setUsername("new-vendor");
        event.setEmail("vendor@test.com");
        event.setStatus("ACTIVE");

        com.tugasakhir.vendor_service.dto.kafka.UserEventDto.RoleDto role = new com.tugasakhir.vendor_service.dto.kafka.UserEventDto.RoleDto();
        role.setName("ROLE_VENDOR");
        event.setRoles(java.util.List.of(role));

        when(repository.findById("user-123")).thenReturn(Optional.empty());
        when(repository.save(any(VendorAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.syncVendorFromUser(event);

        verify(repository).save(argThat(vendor -> vendor.getVendorId().equals("user-123") &&
                vendor.getNamaPerusahaan().equals("new-vendor") &&
                vendor.getEmailKontak().equals("vendor@test.com") &&
                vendor.getStatus().equals("ACTIVE")));
    }

    @Test
    void syncVendorFromUser_UpdateExistingVendor() {
        com.tugasakhir.vendor_service.dto.kafka.UserEventDto event = new com.tugasakhir.vendor_service.dto.kafka.UserEventDto();
        event.setUserId("user-123");
        event.setUsername("updated-vendor");
        event.setEmail("updated@test.com");
        event.setStatus("INACTIVE");

        com.tugasakhir.vendor_service.dto.kafka.UserEventDto.RoleDto role = new com.tugasakhir.vendor_service.dto.kafka.UserEventDto.RoleDto();
        role.setName("ROLE_VENDOR");
        event.setRoles(java.util.List.of(role));

        VendorAccount existing = new VendorAccount();
        existing.setVendorId("user-123");
        existing.setNamaPerusahaan("old-name");
        existing.setEmailKontak("old@test.com");
        existing.setStatus("ACTIVE");

        when(repository.findById("user-123")).thenReturn(Optional.of(existing));
        when(repository.save(any(VendorAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.syncVendorFromUser(event);

        verify(repository).save(argThat(vendor -> vendor.getVendorId().equals("user-123") &&
                vendor.getEmailKontak().equals("updated@test.com") &&
                vendor.getStatus().equals("INACTIVE")
        // Name is NOT updated for existing vendors in current logic, only email and
        // status
        ));
    }

    @Test
    void syncVendorFromUser_IgnoreNonVendor() {
        com.tugasakhir.vendor_service.dto.kafka.UserEventDto event = new com.tugasakhir.vendor_service.dto.kafka.UserEventDto();
        event.setRoles(java.util.List.of()); // No roles

        service.syncVendorFromUser(event);

        verify(repository, never()).save(any());
    }
}
