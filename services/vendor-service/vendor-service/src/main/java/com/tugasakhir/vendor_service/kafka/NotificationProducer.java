package com.tugasakhir.vendor_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendNotification(String userId, String message) {
        String payload = String.format("{\"event\": \"notification.send\", \"userId\": \"%s\", \"message\": \"%s\"}",
                userId, message);
        kafkaTemplate.send("notification.send", userId, payload);
        log.info("Sent notification to user {}: {}", userId, message);
    }
}
