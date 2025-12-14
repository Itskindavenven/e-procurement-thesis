package com.tugasakhir.vendor_service.service.rfq;

import com.tugasakhir.vendor_service.dto.rfq.RFQDTO;
import com.tugasakhir.vendor_service.dto.rfq.RFQMapper;
import com.tugasakhir.vendor_service.exception.ResourceNotFoundException;
import com.tugasakhir.vendor_service.model.rfq.RFQ;
import com.tugasakhir.vendor_service.kafka.RFQProducer;
import com.tugasakhir.vendor_service.repository.rfq.RFQRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RFQService {

    private final RFQRepository repository;
    private final RFQMapper mapper;

    private final RFQProducer rfqProducer;
    private final com.tugasakhir.vendor_service.repository.rfq.ClarificationRepository clarificationRepository;

    public List<RFQDTO> getOpenRFQsForVendor(String vendorId) {
        return repository.findByStatusRfq("OPEN").stream()
                .filter(rfq -> rfq.getVendorId() == null || rfq.getVendorId().equals(vendorId))
                .filter(rfq -> rfq.getTenggatRespon().isAfter(java.time.LocalDateTime.now()))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public RFQDTO getRFQById(String id) {
        RFQ rfq = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RFQ not found: " + id));
        return mapper.toDto(rfq);
    }

    @Transactional
    public void createRFQFromEvent(RFQDTO dto) {
        RFQ entity = mapper.toEntity(dto);
        repository.save(entity);
    }

    @Transactional
    public void declineRFQ(String rfqId, String vendorId, String reason) {
        RFQ rfq = repository.findById(rfqId)
                .orElseThrow(() -> new ResourceNotFoundException("RFQ not found: " + rfqId));

        if (rfq.getTenggatRespon().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalStateException("Cannot decline expired RFQ");
        }

        // In a real scenario, we might update a status in a join table or similar,
        // but for now we just emit the event as the requirement focuses on the action.
        rfqProducer.sendRFQDeclinedEvent(rfqId, vendorId, reason);
    }

    @Transactional
    public void requestClarification(String rfqId, String vendorId, String question) {
        RFQ rfq = repository.findById(rfqId)
                .orElseThrow(() -> new ResourceNotFoundException("RFQ not found: " + rfqId));

        com.tugasakhir.vendor_service.model.rfq.Clarification clarification = new com.tugasakhir.vendor_service.model.rfq.Clarification();
        clarification.setClarificationId(java.util.UUID.randomUUID().toString());
        clarification.setRfqId(rfqId);
        clarification.setVendorId(vendorId);
        clarification.setQuestion(question);
        clarification.setStatus("OPEN");
        clarification.setTimestamp(java.time.LocalDateTime.now());

        clarificationRepository.save(clarification);
    }
}
