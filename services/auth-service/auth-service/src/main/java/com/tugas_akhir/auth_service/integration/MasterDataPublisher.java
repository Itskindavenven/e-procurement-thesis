package com.tugas_akhir.auth_service.integration;

import com.tugas_akhir.auth_service.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class MasterDataPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "user.events";
    private static final int MAX_RETRIES = 3;

    public void publishUserSyncEvent(String eventType, User user) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", eventType);
        event.put("userId", user.getId());
        event.put("username", user.getUsername());
        event.put("email", user.getEmail());
        event.put("status", user.getStatus());
        event.put("roles", user.getRoles());
        event.put("timestamp", System.currentTimeMillis());

        sendWithRetry(event);
    }

    private void sendWithRetry(Object message) {
        int attempts = 0;
        while (attempts < MAX_RETRIES) {
            try {
                kafkaTemplate.send(TOPIC, message);
                log.info("Published masterdata sync event: {}", message);
                return;
            } catch (Exception e) {
                attempts++;
                log.warn("Failed to publish masterdata event (attempt {}/{}): {}", attempts, MAX_RETRIES,
                        e.getMessage());
                if (attempts >= MAX_RETRIES) {
                    log.error("Failed to publish masterdata event after {} attempts. Event dropped.", MAX_RETRIES, e);
                    // In a real system, save to 'pending_events' table here
                }
                try {
                    Thread.sleep(100 * attempts); // Simple backoff
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
