package com.tugas_akhir.admin_service.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "notification_template_placeholders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplatePlaceholder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "placeholder_id")
    private UUID placeholderId;

    @Column(name = "template_id", nullable = false)
    private UUID templateId;

    @Column(name = "placeholder_key", nullable = false, length = 50)
    private String placeholderKey;

    @Column(length = 100)
    private String description;
}
