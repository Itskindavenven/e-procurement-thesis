package com.tugas_akhir.procurement_service.event.producer;

import com.tugas_akhir.procurement_service.event.dto.ProcurementEvents.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka event producer for procurement events
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProcurementEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topic.pr-submitted}")
    private String prSubmittedTopic;

    @Value("${spring.kafka.topic.pr-approved}")
    private String prApprovedTopic;

    @Value("${spring.kafka.topic.pr-rejected}")
    private String prRejectedTopic;

    @Value("${spring.kafka.topic.pr-returned}")
    private String prReturnedTopic;

    @Value("${spring.kafka.topic.pr-reminder}")
    private String prReminderTopic;

    @Value("${spring.kafka.topic.pr-escalated}")
    private String prEscalatedTopic;

    @Value("${spring.kafka.topic.po-created}")
    private String poCreatedTopic;

    @Value("${spring.kafka.topic.stock-alert}")
    private String stockAlertTopic;

    /**
     * Publish PR submitted event
     */
    public void publishPRSubmitted(PRSubmittedEvent event) {
        log.info("Publishing PR Submitted event for PR: {}", event.getProcurementRequestId());
        kafkaTemplate.send(prSubmittedTopic, event.getProcurementRequestId().toString(), event);
    }

    /**
     * Publish PR approved event
     */
    public void publishPRApproved(PRApprovedEvent event) {
        log.info("Publishing PR Approved event for PR: {}", event.getProcurementRequestId());
        kafkaTemplate.send(prApprovedTopic, event.getProcurementRequestId().toString(), event);
    }

    /**
     * Publish PR rejected event
     */
    public void publishPRRejected(PRRejectedEvent event) {
        log.info("Publishing PR Rejected event for PR: {}", event.getProcurementRequestId());
        kafkaTemplate.send(prRejectedTopic, event.getProcurementRequestId().toString(), event);
    }

    /**
     * Publish PR returned event
     */
    public void publishPRReturned(PRReturnedEvent event) {
        log.info("Publishing PR Returned event for PR: {}", event.getProcurementRequestId());
        kafkaTemplate.send(prReturnedTopic, event.getProcurementRequestId().toString(), event);
    }

    /**
     * Publish PR reminder event
     */
    public void publishPRReminder(PRReminderEvent event) {
        log.info("Publishing PR Reminder event for PR: {}", event.getProcurementRequestId());
        kafkaTemplate.send(prReminderTopic, event.getProcurementRequestId().toString(), event);
    }

    /**
     * Publish PR escalated event
     */
    public void publishPREscalated(PRESCalatedEvent event) {
        log.info("Publishing PR Escalated event for PR: {}", event.getProcurementRequestId());
        kafkaTemplate.send(prEscalatedTopic, event.getProcurementRequestId().toString(), event);
    }

    /**
     * Publish PO created event
     */
    public void publishPOCreated(POCreatedEvent event) {
        log.info("Publishing PO Created event for PO: {}", event.getPoId());
        kafkaTemplate.send(poCreatedTopic, event.getPoId().toString(), event);
    }

    /**
     * Send stock alert event when inventory falls below threshold
     */
    public void sendStockAlert(String sku, Integer currentQuantity) {
        StockAlertEvent event = StockAlertEvent.builder()
                .sku(sku)
                .currentQuantity(currentQuantity)
                .alertTime(java.time.LocalDateTime.now())
                .build();
        log.info("Publishing Stock Alert event for SKU: {}, Current Quantity: {}", sku, currentQuantity);
        kafkaTemplate.send(stockAlertTopic, sku, event);
    }

    // --- New Event Methods for Round 3 ---

    @Value("${spring.kafka.topic.goods-received}")
    private String goodsReceivedTopic;

    public void publishGoodsReceived(GoodsReceivedEvent event) {
        log.info("Publishing Goods Received event for PO: {}", event.getPoId());
        kafkaTemplate.send(goodsReceivedTopic, event.getPoId().toString(), event);
    }

    @Value("${spring.kafka.topic.termin-revision}")
    private String terminRevisionTopic;

    public void publishTerminRevisionRequested(java.util.UUID terminId, String notes) {
        // Stub event for now
        log.info("Publishing Termin Revision Requested for Termin: {}", terminId);
        // kafkaTemplate.send(terminRevisionTopic, terminId.toString(), new
        // TerminRevisionEvent(terminId, notes));
    }

    @Value("${spring.kafka.topic.termin-clarification}")
    private String terminClarificationTopic;

    public void publishTerminClarificationRequested(java.util.UUID terminId, String notes) {
        // Stub event for now
        log.info("Publishing Termin Clarification Requested for Termin: {}", terminId);
        // kafkaTemplate.send(terminClarificationTopic, terminId.toString(), new
        // TerminClarificationEvent(terminId, notes));
    }
}
