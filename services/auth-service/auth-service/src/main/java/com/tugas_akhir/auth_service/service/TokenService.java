package com.tugas_akhir.auth_service.service;

import com.tugas_akhir.auth_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtUtil jwtUtil;

    @Value("${jwt.refresh-token-ttl}")
    private long refreshTokenTtl;

    private static final String REFRESH_PREFIX = "refresh:";
    private static final String BLACKLIST_PREFIX = "blacklist:jwt:";
    private static final String USER_REFRESH_INDEX = "user:%s:refreshTokens";

    public String createRefreshToken(UUID userId) {
        String refreshToken = UUID.randomUUID().toString();
        String key = REFRESH_PREFIX + refreshToken;
        
        // Store refresh token with userId
        redisTemplate.opsForValue().set(key, userId.toString(), refreshTokenTtl, TimeUnit.MILLISECONDS);
        
        // Index refresh token for user
        String indexKey = String.format(USER_REFRESH_INDEX, userId);
        redisTemplate.opsForSet().add(indexKey, refreshToken);
        redisTemplate.expire(indexKey, refreshTokenTtl, TimeUnit.MILLISECONDS);
        
        return refreshToken;
    }

    public UUID validateRefreshToken(String refreshToken) {
        String key = REFRESH_PREFIX + refreshToken;
        String userIdStr = (String) redisTemplate.opsForValue().get(key);
        if (userIdStr == null) {
            return null;
        }
        return UUID.fromString(userIdStr);
    }

    public void revokeRefreshToken(String refreshToken) {
        String key = REFRESH_PREFIX + refreshToken;
        String userIdStr = (String) redisTemplate.opsForValue().get(key);
        
        if (userIdStr != null) {
            redisTemplate.delete(key);
            String indexKey = String.format(USER_REFRESH_INDEX, userIdStr);
            redisTemplate.opsForSet().remove(indexKey, refreshToken);
        }
    }

    public void revokeAllUserTokens(UUID userId) {
        String indexKey = String.format(USER_REFRESH_INDEX, userId);
        Set<Object> refreshTokens = redisTemplate.opsForSet().members(indexKey);
        
        if (refreshTokens != null) {
            for (Object token : refreshTokens) {
                String key = REFRESH_PREFIX + token;
                redisTemplate.delete(key);
            }
            redisTemplate.delete(indexKey);
        }
    }

    public void blacklistAccessToken(String accessToken) {
        String jti = jwtUtil.extractJti(accessToken);
        Date expiration = jwtUtil.extractExpiration(accessToken);
        long ttl = expiration.getTime() - System.currentTimeMillis();
        
        if (ttl > 0) {
            String key = BLACKLIST_PREFIX + jti;
            redisTemplate.opsForValue().set(key, "revoked", ttl, TimeUnit.MILLISECONDS);
        }
    }

    public boolean isTokenBlacklisted(String accessToken) {
        String jti = jwtUtil.extractJti(accessToken);
        String key = BLACKLIST_PREFIX + jti;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
