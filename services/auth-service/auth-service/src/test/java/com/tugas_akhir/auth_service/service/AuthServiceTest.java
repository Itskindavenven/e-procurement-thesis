package com.tugas_akhir.auth_service.service;

import com.tugas_akhir.auth_service.domain.PasswordResetRequest;
import com.tugas_akhir.auth_service.domain.Role;
import com.tugas_akhir.auth_service.domain.User;
import com.tugas_akhir.auth_service.dto.AuthDtos;
import com.tugas_akhir.auth_service.integration.AuditPublisher;
import com.tugas_akhir.auth_service.integration.NotificationPublisher;
import com.tugas_akhir.auth_service.repository.PasswordResetRequestRepository;
import com.tugas_akhir.auth_service.repository.RoleRepository;
import com.tugas_akhir.auth_service.repository.UserRepository;
import com.tugas_akhir.auth_service.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordResetRequestRepository passwordResetRequestRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private TokenService tokenService;
    @Mock
    private AuditPublisher auditPublisher;
    @Mock
    private NotificationPublisher notificationPublisher;

    @InjectMocks
    private AuthService authService;

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

    // --- Login Tests ---

    @Test
    void login_Success() {
        AuthDtos.LoginRequest request = new AuthDtos.LoginRequest("testuser", "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(jwtUtil.generateAccessToken(any(), any(), any())).thenReturn("access-token");
        when(tokenService.createRefreshToken(any())).thenReturn("refresh-token");

        AuthDtos.TokenResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        verify(auditPublisher).publishEvent(eq("LOGIN_SUCCESS"), eq("testuser"), any());
    }

    @Test
    void login_UserNotFound() {
        AuthDtos.LoginRequest request = new AuthDtos.LoginRequest("unknown", "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(request));
    }

    @Test
    void login_InactiveUser() {
        user.setStatus(User.UserStatus.INACTIVE);
        AuthDtos.LoginRequest request = new AuthDtos.LoginRequest("testuser", "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(request));
        assertEquals("User account is not active", exception.getMessage());
    }

    // --- Refresh Token Tests ---

    @Test
    void refresh_Success() {
        AuthDtos.RefreshRequest request = new AuthDtos.RefreshRequest("valid-refresh-token");

        when(tokenService.validateRefreshToken("valid-refresh-token")).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(jwtUtil.generateAccessToken(any(), any(), any())).thenReturn("new-access-token");
        when(tokenService.createRefreshToken(any())).thenReturn("new-refresh-token");

        AuthDtos.TokenResponse response = authService.refresh(request);

        assertNotNull(response);
        assertEquals("new-access-token", response.getAccessToken());
        assertEquals("new-refresh-token", response.getRefreshToken());
        verify(tokenService).revokeRefreshToken("valid-refresh-token");
    }

    @Test
    void refresh_InvalidToken() {
        AuthDtos.RefreshRequest request = new AuthDtos.RefreshRequest("invalid-token");

        when(tokenService.validateRefreshToken("invalid-token")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.refresh(request));
        assertEquals("Invalid or expired refresh token", exception.getMessage());
    }

    // --- Logout Tests ---

    @Test
    void logout_Success() {
        AuthDtos.LogoutRequest request = new AuthDtos.LogoutRequest();
        request.setAccessToken("access-token");
        request.setRefreshToken("refresh-token");

        authService.logout(request);

        verify(tokenService).blacklistAccessToken("access-token");
        verify(tokenService).revokeRefreshToken("refresh-token");
        verify(auditPublisher).publishEvent(eq("LOGOUT"), any(), any());
    }

    // --- Password Reset Tests ---

    @Test
    void requestPasswordReset_Success() {
        AuthDtos.ResetRequest request = new AuthDtos.ResetRequest();
        request.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        authService.requestPasswordReset(request);

        verify(passwordResetRequestRepository).save(any());
        verify(notificationPublisher).sendEmailNotification(eq("password_reset_requested"), eq("test@example.com"),
                any());
        verify(auditPublisher).publishEvent(eq("PASSWORD_RESET_REQUESTED"), eq("testuser"), any());
    }

    @Test
    void requestPasswordReset_UserNotFound() {
        AuthDtos.ResetRequest request = new AuthDtos.ResetRequest();
        request.setEmail("unknown@example.com");

        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.requestPasswordReset(request));
    }

    @Test
    void confirmPasswordReset_Success() {
        AuthDtos.ResetConfirmRequest request = new AuthDtos.ResetConfirmRequest();
        request.setToken("valid-token");
        request.setNewPassword("newPass");
        request.setNewPasswordConfirm("newPass");

        PasswordResetRequest resetRequest = new PasswordResetRequest();
        resetRequest.setUserId(user.getId());
        resetRequest.setToken("valid-token");
        resetRequest.setExpiresAt(LocalDateTime.now().plusHours(1));
        resetRequest.setUsed(false);

        when(passwordResetRequestRepository.findByToken("valid-token")).thenReturn(Optional.of(resetRequest));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

        authService.confirmPasswordReset(request);

        verify(userRepository).save(user);
        verify(tokenService).revokeAllUserTokens(user.getId());
        verify(auditPublisher).publishEvent(eq("PASSWORD_RESET_SUCCESS"), eq("testuser"), any());
        assertTrue(resetRequest.isUsed());
    }

    @Test
    void confirmPasswordReset_MismatchPasswords() {
        AuthDtos.ResetConfirmRequest request = new AuthDtos.ResetConfirmRequest();
        request.setNewPassword("pass1");
        request.setNewPasswordConfirm("pass2");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.confirmPasswordReset(request));
        assertEquals("Passwords do not match", exception.getMessage());
    }

    @Test
    void confirmPasswordReset_ExpiredToken() {
        AuthDtos.ResetConfirmRequest request = new AuthDtos.ResetConfirmRequest();
        request.setToken("expired-token");
        request.setNewPassword("pass");
        request.setNewPasswordConfirm("pass");

        PasswordResetRequest resetRequest = new PasswordResetRequest();
        resetRequest.setExpiresAt(LocalDateTime.now().minusHours(1)); // Expired

        when(passwordResetRequestRepository.findByToken("expired-token")).thenReturn(Optional.of(resetRequest));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.confirmPasswordReset(request));
        assertEquals("Token expired or already used", exception.getMessage());
    }
}
