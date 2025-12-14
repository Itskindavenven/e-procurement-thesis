package com.tugas_akhir.auth_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic auditEventsTopic() {
        return TopicBuilder.name("audit.events")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic notificationEmailTopic() {
        return TopicBuilder.name("notification.email")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
