package com.tugas_akhir.procurement_service.domain.additionaldocument.service;

import com.tugas_akhir.procurement_service.domain.additionaldocument.entity.AdditionalDocument;
import com.tugas_akhir.procurement_service.domain.additionaldocument.repository.AdditionalDocumentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.tugas_akhir.procurement_service.domain.procurementrequest.dto.ProcurementRequestDTOs.AdditionalDocumentDTO;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;
import com.tugas_akhir.procurement_service.domain.procurementrequest.repository.ProcurementRequestRepository;
import com.tugas_akhir.procurement_service.mapper.ProcurementMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final AdditionalDocumentRepository repository;
    private final ProcurementRequestRepository requestRepository;
    private final ProcurementMapper mapper;

    @Transactional
    public AdditionalDocumentDTO uploadDocument(UUID requestId, AdditionalDocumentDTO dto) {
        ProcurementRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        AdditionalDocument entity = mapper.toEntity(dto);
        entity.setProcurementRequest(request);

        AdditionalDocument saved = repository.save(entity);
        return mapper.toDTO(saved);
    }
}

