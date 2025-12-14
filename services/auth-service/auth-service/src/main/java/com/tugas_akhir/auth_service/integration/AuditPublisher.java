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
public class AuditPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "audit.events";

    public void publishEvent(String eventType, String username, String details) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", eventType);
        event.put("username", username);
        event.put("details", details);
        event.put("timestamp", System.currentTimeMillis());

        try {
            kafkaTemplate.send(TOPIC, event);
            log.info("Published audit event: {}", event);
        } catch (Exception e) {
            log.error("Failed to publish audit event", e);
            // In a real app, we would fallback to DB
        }
    }
}
