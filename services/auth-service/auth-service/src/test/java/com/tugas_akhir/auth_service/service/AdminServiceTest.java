package com.tugas_akhir.auth_service.service;

import com.tugas_akhir.auth_service.domain.Role;
import com.tugas_akhir.auth_service.domain.User;
import com.tugas_akhir.auth_service.integration.AuditPublisher;
import com.tugas_akhir.auth_service.integration.MasterDataPublisher;
import com.tugas_akhir.auth_service.repository.RoleRepository;
import com.tugas_akhir.auth_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenService tokenService;
    @Mock
    private AuditPublisher auditPublisher;
    @Mock
    private MasterDataPublisher masterDataPublisher;

    @InjectMocks
    private AdminService adminService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("hashedpassword");
        user.setStatus(User.UserStatus.ACTIVE);
        user.setRoles(Set.of(new Role(1L, "ROLE_USER")));
    }

    @Test
    void createUser_Success() {
        String username = "newuser";
        String email = "new@example.com";
        String password = "password";
        List<String> roles = List.of("ROLE_USER");

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("encoded");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(new Role(1L, "ROLE_USER")));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User createdUser = adminService.createUser(username, email, password, roles);

        assertNotNull(createdUser);
        assertEquals(username, createdUser.getUsername());
        assertEquals(email, createdUser.getEmail());
        assertEquals(User.UserStatus.ACTIVE, createdUser.getStatus());

        verify(auditPublisher).publishEvent(eq("USER_ACCOUNT_CREATED"), eq("admin"), any());
        verify(masterDataPublisher).publishUserSyncEvent(eq("USER_CREATED"), any(User.class));
    }

    @Test
    void createUser_AlreadyExists() {
        String username = "existing";
        String email = "new@example.com";
        String password = "password";
        List<String> roles = List.of("ROLE_USER");

        when(userRepository.existsByUsername(username)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> adminService.createUser(username, email, password, roles));
        assertEquals("Username or Email already exists", exception.getMessage());
    }

    @Test
    void updateUser_Success() {
        UUID userId = user.getId();
        String newEmail = "updated@example.com";
        List<String> newRoles = List.of("ROLE_ADMIN");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(new Role(2L, "ROLE_ADMIN")));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = adminService.updateUser(userId, newEmail, newRoles);

        assertEquals(newEmail, updatedUser.getEmail());
        assertTrue(updatedUser.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN")));

        verify(auditPublisher).publishEvent(eq("USER_ACCOUNT_UPDATED"), eq("admin"), any());
        verify(masterDataPublisher).publishUserSyncEvent(eq("USER_UPDATED"), any(User.class));
    }

    @Test
    void deactivateUser_Success() {
        UUID userId = user.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        adminService.deactivateUser(userId);

        assertEquals(User.UserStatus.INACTIVE, user.getStatus());
        verify(tokenService).revokeAllUserTokens(userId);
        verify(auditPublisher).publishEvent(eq("USER_ACCOUNT_DEACTIVATED"), eq("admin"), any());
        verify(masterDataPublisher).publishUserSyncEvent(eq("USER_DEACTIVATED"), any(User.class));
    }

    @Test
    void reactivateUser_Success() {
        user.setStatus(User.UserStatus.INACTIVE);
        UUID userId = user.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        adminService.reactivateUser(userId);

        assertEquals(User.UserStatus.ACTIVE, user.getStatus());
        verify(auditPublisher).publishEvent(eq("USER_ACCOUNT_REACTIVATE"), eq("admin"), any());
        verify(masterDataPublisher).publishUserSyncEvent(eq("USER_REACTIVATED"), any(User.class));
    }
}
