package com.tugas_akhir.procurement_service.service;

import com.tugas_akhir.procurement_service.common.enums.ProcurementStatus;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;
import com.tugas_akhir.procurement_service.domain.procurementrequest.repository.ProcurementRequestRepository;
import com.tugas_akhir.procurement_service.event.producer.ProcurementEventProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for scheduled background tasks
 * Handles auto-reminder (24h) and auto-escalation (48h) for pending approvals
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTaskService {

    private final ProcurementRequestRepository procurementRequestRepository;
    private final ProcurementEventProducer eventProducer;

    /**
     * Auto-reminder: Send reminder to Supervisor after 24 hours
     * Runs every hour
     */
    @Scheduled(fixedDelay = 3600000) // 1 hour = 3600000ms
    public void sendAutoReminders() {
        log.info("Running auto-reminder task");

        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
        LocalDateTime fortyEightHoursAgo = LocalDateTime.now().minusHours(48);

        // Find submitted PRs between 24-48 hours old (not yet escalated)
        List<ProcurementRequest> prsForReminder = procurementRequestRepository
                .findByStatusAndCreatedAtBefore(ProcurementStatus.SUBMITTED, twentyFourHoursAgo);

        int reminderCount = 0;
        for (ProcurementRequest pr : prsForReminder) {
            // Only send reminder if not yet 48 hours (escalation threshold)
            if (pr.getCreatedAt().isAfter(fortyEightHoursAgo)) {
                sendReminderEvent(pr);
                reminderCount++;
            }
        }

        log.info("Auto-reminder task completed. Sent {} reminders", reminderCount);
    }

    /**
     * Auto-escalation: Escalate to Admin after 48 hours of no response
     * Runs every hour
     */
    @Scheduled(fixedDelay = 3600000) // 1 hour = 3600000ms
    public void escalateStaleApprovals() {
        log.info("Running auto-escalation task");

        LocalDateTime fortyEightHoursAgo = LocalDateTime.now().minusHours(48);

        // Find submitted PRs older than 48 hours
        List<ProcurementRequest> prsForEscalation = procurementRequestRepository
                .findByStatusAndCreatedAtBefore(ProcurementStatus.SUBMITTED, fortyEightHoursAgo);

        int escalationCount = 0;
        for (ProcurementRequest pr : prsForEscalation) {
            escalatePR(pr);
            escalationCount++;
        }

        log.info("Auto-escalation task completed. Escalated {} PRs", escalationCount);
    }

    /**
     * Send reminder event for Supervisor
     */
    private void sendReminderEvent(ProcurementRequest pr) {
        try {
            // TODO: Create and publish reminder event
            // ProcurementEvents.PRReminderEvent event = createReminderEvent(pr);
            // eventProducer.publishPRReminder(event);

            log.info("Sent reminder for PR: {}", pr.getId());
        } catch (Exception e) {
            log.error("Failed to send reminder for PR: {}", pr.getId(), e);
        }
    }

    /**
     * Escalate PR to Admin
     */
    private void escalatePR(ProcurementRequest pr) {
        try {
            // Update status to escalated
            pr.setStatus(ProcurementStatus.ESCALATED);
            procurementRequestRepository.save(pr);

            // TODO: Create and publish escalation event
            // ProcurementEvents.PRESCalatedEvent event = createEscalationEvent(pr);
            // eventProducer.publishPREscalated(event);

            log.info("Escalated PR to Admin: {}", pr.getId());
        } catch (Exception e) {
            log.error("Failed to escalate PR: {}", pr.getId(), e);
        }
    }
}
