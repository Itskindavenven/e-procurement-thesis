package com.tugas_akhir.procurement_service.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

/**
 * Security utility class for JWT operations
 */
public class SecurityUtils {

    private SecurityUtils() {
        // Utility class
    }

    /**
     * Get current authenticated JWT token
     */
    public static Jwt getCurrentJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            return (Jwt) authentication.getPrincipal();
        }
        throw new IllegalStateException("No JWT token found in security context");
    }

    /**
     * Extract user ID from JWT token
     */
    public static UUID getUserIdFromJwt(Jwt jwt) {
        String subject = jwt.getSubject();
        if (subject == null) {
            throw new IllegalArgumentException("JWT token does not contain subject (user ID)");
        }
        return UUID.fromString(subject);
    }

    /**
     * Get current authenticated user ID
     */
    public static UUID getCurrentUserId() {
        return getUserIdFromJwt(getCurrentJwt());
    }

    /**
     * Extract username from JWT token
     */
    public static String getUsernameFromJwt(Jwt jwt) {
        return jwt.getClaimAsString("username");
    }

    /**
     * Get current authenticated username
     */
    public static String getCurrentUsername() {
        return getUsernameFromJwt(getCurrentJwt());
    }

    /**
     * Extract roles from JWT token
     */
    public static List<String> getRolesFromJwt(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        return roles != null ? roles : List.of();
    }

    /**
     * Get current authenticated user roles
     */
    public static List<String> getCurrentUserRoles() {
        return getRolesFromJwt(getCurrentJwt());
    }

    /**
     * Check if JWT has specific role
     */
    public static boolean hasRole(Jwt jwt, String role) {
        List<String> roles = getRolesFromJwt(jwt);
        String roleToCheck = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return roles.stream()
                .anyMatch(r -> r.equals(roleToCheck) || r.equals(role));
    }

    /**
     * Check if current user has specific role
     */
    public static boolean currentUserHasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        String roleToCheck = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals(roleToCheck));
    }

    /**
     * Validate that header user ID matches JWT user ID
     * Used for backward compatibility during migration
     */
    public static void validateUserIdMatch(UUID headerUserId, Jwt jwt) {
        UUID jwtUserId = getUserIdFromJwt(jwt);
        if (!jwtUserId.equals(headerUserId)) {
            throw new SecurityException(
                    String.format("X-User-Id header (%s) does not match JWT subject (%s)",
                            headerUserId, jwtUserId));
        }
    }
}
