package com.tugas_akhir.admin_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final WebClient webClient;

    @Value("${auth.service.url:http://localhost:8081}")
    private String authServiceUrl;

    public JwtAuthenticationFilter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // Call Auth Service to validate token
                // Assuming Auth Service returns details including username and roles if valid
                Map<String, Object> userDetails = webClient.get()
                        .uri(authServiceUrl + "/api/auth/validate?token=" + token)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block(); // Blocking here because we are in a Servlet filter

                if (userDetails != null && (Boolean) userDetails.get("valid")) {
                    String username = (String) userDetails.get("username");
                    List<String> roles = (List<String>) userDetails.get("roles");

                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Token validation failed or Auth Service is down
                // We can log this, but we shouldn't fail the request immediately,
                // just don't set authentication context. Spring Security will handle 401/403.
                logger.error("Token validation failed", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
