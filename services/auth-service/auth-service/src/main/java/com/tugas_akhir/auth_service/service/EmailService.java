package com.tugas_akhir.auth_service.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
