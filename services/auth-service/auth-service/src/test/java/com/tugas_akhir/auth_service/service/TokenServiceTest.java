package com.tugas_akhir.auth_service.service;

import com.tugas_akhir.auth_service.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ValueOperations<String, Object> valueOperations;
    @Mock
    private SetOperations<String, Object> setOperations;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        // Only mock setOperations if needed by the specific test method
        ReflectionTestUtils.setField(tokenService, "refreshTokenTtl", 3600000L);
    }

    @Test
    void createRefreshToken_Success() {
        UUID userId = UUID.randomUUID();
        when(redisTemplate.opsForSet()).thenReturn(setOperations);

        String token = tokenService.createRefreshToken(userId);

        assertNotNull(token);
        verify(valueOperations).set(startsWith("refresh:"), eq(userId.toString()), anyLong(),
                eq(TimeUnit.MILLISECONDS));
        verify(setOperations).add(contains("refreshTokens"), anyString());
    }

    @Test
    void validateRefreshToken_Success() {
        String token = "valid-token";
        UUID userId = UUID.randomUUID();
        when(valueOperations.get("refresh:" + token)).thenReturn(userId.toString());

        UUID result = tokenService.validateRefreshToken(token);

        assertEquals(userId, result);
    }

    @Test
    void validateRefreshToken_NotFound() {
        String token = "invalid-token";
        when(valueOperations.get("refresh:" + token)).thenReturn(null);

        UUID result = tokenService.validateRefreshToken(token);

        assertNull(result);
    }

    @Test
    void revokeRefreshToken_Success() {
        String token = "valid-token";
        UUID userId = UUID.randomUUID();
        when(valueOperations.get("refresh:" + token)).thenReturn(userId.toString());
        when(redisTemplate.opsForSet()).thenReturn(setOperations);

        tokenService.revokeRefreshToken(token);

        verify(redisTemplate).delete("refresh:" + token);
        verify(setOperations).remove(contains("refreshTokens"), eq(token));
    }

    @Test
    void blacklistAccessToken_Success() {
        String accessToken = "jwt-token";
        String jti = "jti-123";
        Date expiration = new Date(System.currentTimeMillis() + 10000); // 10s future

        when(jwtUtil.extractJti(accessToken)).thenReturn(jti);
        when(jwtUtil.extractExpiration(accessToken)).thenReturn(expiration);

        tokenService.blacklistAccessToken(accessToken);

        verify(valueOperations).set(eq("blacklist:jwt:" + jti), eq("revoked"), anyLong(), eq(TimeUnit.MILLISECONDS));
    }
}
