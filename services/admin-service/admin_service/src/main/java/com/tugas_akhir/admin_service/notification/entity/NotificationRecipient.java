package com.tugas_akhir.admin_service.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "notification_recipients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRecipient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "recipient_id")
    private UUID recipientId;

    @Column(name = "log_id", nullable = false)
    private UUID logId;

    @Column(name = "recipient_email", nullable = false, length = 100)
    private String recipientEmail;

    @Column(name = "recipient_type", length = 20)
    private String recipientType; // TO, CC, BCC
}
