package com.tugas_akhir.auth_service.service;

import com.tugas_akhir.auth_service.domain.PasswordResetRequest;
import com.tugas_akhir.auth_service.domain.Role;
import com.tugas_akhir.auth_service.domain.User;
import com.tugas_akhir.auth_service.dto.AuthDtos.*;
import com.tugas_akhir.auth_service.integration.AuditPublisher;
import com.tugas_akhir.auth_service.integration.NotificationPublisher;
import com.tugas_akhir.auth_service.repository.PasswordResetRequestRepository;
import com.tugas_akhir.auth_service.repository.RoleRepository;
import com.tugas_akhir.auth_service.repository.UserRepository;
import com.tugas_akhir.auth_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordResetRequestRepository passwordResetRequestRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private final AuditPublisher auditPublisher;
    private final NotificationPublisher notificationPublisher;

    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getStatus() != User.UserStatus.ACTIVE) {
            throw new RuntimeException("User account is not active");
        }

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername(), roles);
        String refreshToken = tokenService.createRefreshToken(user.getId());

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        auditPublisher.publishEvent("LOGIN_SUCCESS", user.getUsername(), "User logged in successfully");

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refresh(RefreshRequest request) {
        UUID userId = tokenService.validateRefreshToken(request.getRefreshToken());
        if (userId == null) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Rotate tokens
        tokenService.revokeRefreshToken(request.getRefreshToken());
        
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String newAccessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername(), roles);
        String newRefreshToken = tokenService.createRefreshToken(user.getId());

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    public void logout(LogoutRequest request) {
        if (request.getAccessToken() != null) {
            tokenService.blacklistAccessToken(request.getAccessToken());
        }
        if (request.getRefreshToken() != null) {
            tokenService.revokeRefreshToken(request.getRefreshToken());
        }
        // Audit handled in controller or here
        auditPublisher.publishEvent("LOGOUT", "unknown", "User logged out");
    }

    @Transactional
    public void requestPasswordReset(ResetRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        PasswordResetRequest resetRequest = new PasswordResetRequest();
        resetRequest.setUserId(user.getId());
        resetRequest.setToken(token);
        resetRequest.setExpiresAt(LocalDateTime.now().plusHours(1));
        resetRequest.setUsed(false);
        resetRequest.setStatus(PasswordResetRequest.ResetStatus.PENDING);
        
        passwordResetRequestRepository.save(resetRequest);

        notificationPublisher.sendEmailNotification("password_reset_requested", user.getEmail(), Map.of("token", token));
        auditPublisher.publishEvent("PASSWORD_RESET_REQUESTED", user.getUsername(), "Password reset requested");
    }

    @Transactional
    public void confirmPasswordReset(ResetConfirmRequest request) {
        if (!request.getNewPassword().equals(request.getNewPasswordConfirm())) {
            throw new RuntimeException("Passwords do not match");
        }

        PasswordResetRequest resetRequest = passwordResetRequestRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetRequest.isUsed() || resetRequest.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired or already used");
        }

        User user = userRepository.findById(resetRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        resetRequest.setUsed(true);
        resetRequest.setStatus(PasswordResetRequest.ResetStatus.USED);
        passwordResetRequestRepository.save(resetRequest);

        // Revoke all sessions
        tokenService.revokeAllUserTokens(user.getId());

        auditPublisher.publishEvent("PASSWORD_RESET_SUCCESS", user.getUsername(), "Password reset successful");
    }
}
