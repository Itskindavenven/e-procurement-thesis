package com.tugas_akhir.auth_service.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC_PREFIX = "notification.email.";

    public void sendEmailNotification(String type, String to, Map<String, Object> data) {
        Map<String, Object> message = new HashMap<>();
        message.put("to", to);
        message.put("data", data);
        message.put("timestamp", System.currentTimeMillis());

        String topic = TOPIC_PREFIX + type;

        try {
            kafkaTemplate.send(topic, message);
            log.info("Published notification event to {}: {}", topic, message);
        } catch (Exception e) {
            log.error("Failed to publish notification event", e);
        }
    }
}
