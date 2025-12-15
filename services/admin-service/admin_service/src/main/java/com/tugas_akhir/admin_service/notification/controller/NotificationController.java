package com.tugas_akhir.admin_service.notification.controller;

import com.tugas_akhir.admin_service.notification.dto.NotificationLogDTO;
import com.tugas_akhir.admin_service.notification.dto.NotificationTemplateDTO;
import com.tugas_akhir.admin_service.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/templates")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationTemplateDTO>> getAllTemplates() {
        return ResponseEntity.ok(notificationService.getAllTemplates());
    }

    @GetMapping("/templates/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationTemplateDTO> getTemplateById(@PathVariable UUID id) {
        return ResponseEntity.ok(notificationService.getTemplateById(id));
    }

    @PostMapping("/templates")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationTemplateDTO> createTemplate(@Valid @RequestBody NotificationTemplateDTO dto) {
        return new ResponseEntity<>(notificationService.createTemplate(dto), HttpStatus.CREATED);
    }

    @PutMapping("/templates/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationTemplateDTO> updateTemplate(@PathVariable UUID id,
            @Valid @RequestBody NotificationTemplateDTO dto) {
        return ResponseEntity.ok(notificationService.updateTemplate(id, dto));
    }

    @GetMapping("/logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationLogDTO>> getLogs() {
        return ResponseEntity.ok(notificationService.getLogs());
    }

    @GetMapping("/recipients")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<String>> getRecipients(@RequestParam UUID templateId) {
        return ResponseEntity.ok(notificationService.getRecipients(templateId));
    }

    @PostMapping("/recipients")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateRecipients(@RequestBody java.util.Map<String, Object> payload) {
        UUID templateId = UUID.fromString((String) payload.get("templateId"));
        List<String> recipients = (List<String>) payload.get("recipients");
        notificationService.updateRecipients(templateId, recipients);
        return ResponseEntity.ok().build();
    }
}
