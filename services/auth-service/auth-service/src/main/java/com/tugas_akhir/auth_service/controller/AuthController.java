package com.tugas_akhir.auth_service.controller;

import com.tugas_akhir.auth_service.dto.AuthDtos.*;
import com.tugas_akhir.auth_service.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest servletRequest,
            @RequestBody(required = false) LogoutRequest request) {
        String authHeader = servletRequest.getHeader("Authorization");
        String accessToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.substring(7);
        }

        if (request == null) {
            request = new LogoutRequest();
        }
        if (request.getAccessToken() == null) {
            request.setAccessToken(accessToken);
        }

        authService.logout(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset/request")
    public ResponseEntity<Void> requestReset(@Valid @RequestBody ResetRequest request) {
        authService.requestPasswordReset(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset/confirm")
    public ResponseEntity<Void> confirmReset(@Valid @RequestBody ResetConfirmRequest request) {
        authService.confirmPasswordReset(request);
        return ResponseEntity.ok().build();
    }
}
