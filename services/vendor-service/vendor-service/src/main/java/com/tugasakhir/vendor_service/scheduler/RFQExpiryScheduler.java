package com.tugasakhir.vendor_service.scheduler;

import com.tugasakhir.vendor_service.model.rfq.RFQ;
import com.tugasakhir.vendor_service.repository.rfq.RFQRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RFQExpiryScheduler {

    private final RFQRepository rfqRepository;

    @Scheduled(cron = "0 0 * * * *") // Run every hour
    @Transactional
    public void closeExpiredRFQs() {
        log.info("Running RFQ expiry check...");
        List<RFQ> openRFQs = rfqRepository.findByStatusRfq("OPEN");
        LocalDateTime now = LocalDateTime.now();

        for (RFQ rfq : openRFQs) {
            if (rfq.getTenggatRespon() != null && rfq.getTenggatRespon().isBefore(now)) {
                rfq.setStatusRfq("CLOSED");
                rfqRepository.save(rfq);
                log.info("Closed expired RFQ: {}", rfq.getRfqId());
            }
        }
    }
}
