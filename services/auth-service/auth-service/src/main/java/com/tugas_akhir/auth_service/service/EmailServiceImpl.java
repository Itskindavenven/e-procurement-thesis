package com.tugas_akhir.auth_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(name = "email.enabled", havingValue = "false", matchIfMissing = true)
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendEmail(String to, String subject, String body) {
        log.info("STUB EMAIL SENT to: {}, subject: {}, body: {}", to, subject, body);
    }
}
