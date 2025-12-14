package com.tugas_akhir.auth_service.service;

import com.tugas_akhir.auth_service.domain.Role;
import com.tugas_akhir.auth_service.domain.User;
import com.tugas_akhir.auth_service.domain.VendorProfile;
import com.tugas_akhir.auth_service.integration.AuditPublisher;
import com.tugas_akhir.auth_service.integration.MasterDataPublisher;
import com.tugas_akhir.auth_service.integration.NotificationPublisher;
import com.tugas_akhir.auth_service.repository.RoleRepository;
import com.tugas_akhir.auth_service.repository.UserRepository;
import com.tugas_akhir.auth_service.repository.VendorProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VendorServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private VendorProfileRepository vendorProfileRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuditPublisher auditPublisher;
    @Mock
    private NotificationPublisher notificationPublisher;
    @Mock
    private MasterDataPublisher masterDataPublisher;

    @InjectMocks
    private VendorService vendorService;

    @Test
    void registerVendor_Success() {
        String companyName = "Acme Corp";
        String npwp = "123456789";
        String address = "123 Main St";
        String email = "vendor@acme.com";
        String phone = "555-1234";
        MultipartFile[] documents = new MultipartFile[] {};

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPass");
        when(roleRepository.findByName("ROLE_VENDOR")).thenReturn(Optional.of(new Role(1L, "ROLE_VENDOR")));

        // Capture the user saved
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(java.util.UUID.randomUUID());
            return u;
        });

        vendorService.registerVendor(companyName, npwp, address, email, phone, documents);

        verify(userRepository).save(any(User.class));
        verify(vendorProfileRepository).save(any(VendorProfile.class));
        verify(auditPublisher).publishEvent(eq("VENDOR_REGISTRATION_PENDING"), eq(email), any());
        verify(notificationPublisher).sendEmailNotification(eq("vendor_registration"), eq(email), any());
        verify(masterDataPublisher).publishUserSyncEvent(eq("USER_CREATED"), any(User.class));
    }

    @Test
    void registerVendor_EmailExists() {
        String email = "existing@acme.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> vendorService.registerVendor("Co", "123", "Addr", email, "Phone", null));

        assertEquals("Email already registered", exception.getMessage());
    }
}
