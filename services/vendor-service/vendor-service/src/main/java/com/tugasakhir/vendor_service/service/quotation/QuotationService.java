package com.tugasakhir.vendor_service.service.quotation;

import com.tugasakhir.vendor_service.dto.quotation.QuotationDTO;
import com.tugasakhir.vendor_service.dto.quotation.QuotationMapper;
import com.tugasakhir.vendor_service.model.quotation.QuotationResponse;
import com.tugasakhir.vendor_service.repository.quotation.QuotationRepository;
import com.tugasakhir.vendor_service.kafka.RFQProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuotationService {

    private final QuotationRepository repository;
    private final QuotationMapper mapper;

    private final RFQProducer rfqProducer;

    public QuotationDTO getQuotationByRfqAndVendor(String rfqId, String vendorId) {
        return repository.findByRfqIdAndVendorId(rfqId, vendorId)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Transactional
    public QuotationDTO submitQuotation(QuotationDTO dto) {
        if (dto.getTotalHarga().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total price cannot be negative");
        }

        java.util.Optional<QuotationResponse> existingOpt = repository.findByRfqIdAndVendorId(dto.getRfqId(),
                dto.getVendorId());

        QuotationResponse entity;
        boolean isRevision = false;

        if (existingOpt.isPresent()) {
            entity = existingOpt.get();
            entity.setTotalHarga(dto.getTotalHarga());
            entity.setRincianHarga(dto.getRincianHarga());
            entity.setLeadTime(dto.getLeadTime());
            entity.setCatatanVendor(dto.getCatatanVendor());
            entity.setStatusQuote("REVISED");
            // Version is handled by @Version or manual increment if we want logical
            // versioning
            // Since we added a manual 'version' field, let's increment it.
            entity.setVersion(entity.getVersion() == null ? 1 : entity.getVersion() + 1);
            isRevision = true;
        } else {
            if (dto.getQuoteId() == null) {
                dto.setQuoteId(UUID.randomUUID().toString());
            }
            dto.setTimestamp(LocalDateTime.now());
            dto.setStatusQuote("SUBMITTED");
            dto.setVersion(1);
            entity = mapper.toEntity(dto);
        }

        QuotationResponse saved = repository.save(entity);

        if (isRevision) {
            rfqProducer.sendRFQRevisedEvent(saved.getRfqId(), saved.getVendorId(), saved.getVersion());
        } else {
            rfqProducer.sendRFQRespondedEvent(saved.getRfqId(), saved.getVendorId());
        }

        return mapper.toDto(saved);
    }
}
