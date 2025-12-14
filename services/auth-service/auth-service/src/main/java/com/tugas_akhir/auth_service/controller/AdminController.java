package com.tugas_akhir.auth_service.controller;

import com.tugas_akhir.auth_service.domain.User;
import com.tugas_akhir.auth_service.service.AdminService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<Page<User>> getUsers(Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllUsers(pageable));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminService.createUser(request.getUsername(), request.getEmail(), request.getPassword(),
                        request.getRoles()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(adminService.updateUser(id, request.getEmail(), request.getRoles()));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID id) {
        adminService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reactivate")
    public ResponseEntity<Void> reactivateUser(@PathVariable UUID id) {
        adminService.reactivateUser(id);
        return ResponseEntity.ok().build();
    }

    @Data
    public static class CreateUserRequest {
        private String username;
        private String email;
        private String password;
        private List<String> roles;
    }

    @Data
    public static class UpdateUserRequest {
        private String email;
        private List<String> roles;
    }
}
