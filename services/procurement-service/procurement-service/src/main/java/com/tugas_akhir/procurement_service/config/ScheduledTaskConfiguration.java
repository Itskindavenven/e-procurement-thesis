package com.tugas_akhir.procurement_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuration for scheduled tasks
 */
@Configuration
@EnableScheduling
public class ScheduledTaskConfiguration {
    // Enables @Scheduled annotations for auto-reminder and auto-escalation
}
