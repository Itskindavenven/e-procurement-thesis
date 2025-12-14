package com.tugas_akhir.auth_service.service;

import com.tugas_akhir.auth_service.domain.Role;
import com.tugas_akhir.auth_service.domain.User;
import com.tugas_akhir.auth_service.dto.AuthDtos;
import com.tugas_akhir.auth_service.integration.AuditPublisher;
import com.tugas_akhir.auth_service.integration.MasterDataPublisher;
import com.tugas_akhir.auth_service.repository.RoleRepository;
import com.tugas_akhir.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuditPublisher auditPublisher;
    private final MasterDataPublisher masterDataPublisher;

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional
    public User createUser(String username, String email, String password, List<String> roles) {
        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
            throw new RuntimeException("Username or Email already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setStatus(User.UserStatus.ACTIVE);

        Set<Role> userRoles = new HashSet<>();
        for (String roleName : roles) {
            Role role = roleRepository.findByName(roleName)
                    .orElseGet(() -> roleRepository.save(new Role(null, roleName)));
            userRoles.add(role);
        }
        user.setRoles(userRoles);

        User savedUser = userRepository.save(user);
        auditPublisher.publishEvent("USER_ACCOUNT_CREATED", "admin", "Created user " + username);
        masterDataPublisher.publishUserSyncEvent("USER_CREATED", savedUser);
        return savedUser;
    }

    @Transactional
    public User updateUser(UUID id, String email, List<String> roles) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmail(email);

        if (roles != null && !roles.isEmpty()) {
            Set<Role> userRoles = new HashSet<>();
            for (String roleName : roles) {
                Role role = roleRepository.findByName(roleName)
                        .orElseGet(() -> roleRepository.save(new Role(null, roleName)));
                userRoles.add(role);
            }
            user.setRoles(userRoles);
        }

        User savedUser = userRepository.save(user);
        auditPublisher.publishEvent("USER_ACCOUNT_UPDATED", "admin", "Updated user " + user.getUsername());
        masterDataPublisher.publishUserSyncEvent("USER_UPDATED", savedUser);
        return savedUser;
    }

    @Transactional
    public void deactivateUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(User.UserStatus.INACTIVE);
        userRepository.save(user);

        tokenService.revokeAllUserTokens(id);

        auditPublisher.publishEvent("USER_ACCOUNT_DEACTIVATED", "admin", "Deactivated user " + user.getUsername());
        masterDataPublisher.publishUserSyncEvent("USER_DEACTIVATED", user);
    }

    @Transactional
    public void reactivateUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(User.UserStatus.ACTIVE);
        userRepository.save(user);

        auditPublisher.publishEvent("USER_ACCOUNT_REACTIVATE", "admin", "Reactivated user " + user.getUsername());
        masterDataPublisher.publishUserSyncEvent("USER_REACTIVATED", user);
    }

    @Transactional
    public void updateUserStatus(UUID id, User.UserStatus status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(status);
        userRepository.save(user);

        auditPublisher.publishEvent("USER_STATUS_UPDATED", "admin",
                "Updated user " + user.getUsername() + " status to " + status);
        masterDataPublisher.publishUserSyncEvent("USER_UPDATED", user);
    }
}
