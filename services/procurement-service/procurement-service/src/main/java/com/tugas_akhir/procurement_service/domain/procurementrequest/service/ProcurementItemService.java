package com.tugas_akhir.procurement_service.domain.procurementrequest.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.tugas_akhir.procurement_service.domain.procurementrequest.dto.ProcurementItemDTO;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementItem;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;
import com.tugas_akhir.procurement_service.domain.procurementrequest.repository.ProcurementItemRepository;
import com.tugas_akhir.procurement_service.domain.procurementrequest.repository.ProcurementRequestRepository;
import com.tugas_akhir.procurement_service.mapper.ProcurementMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcurementItemService {

    private final ProcurementItemRepository repository;
    private final ProcurementRequestRepository requestRepository;
    private final ProcurementMapper mapper;

    @Transactional
    public ProcurementItemDTO addItem(UUID requestId, ProcurementItemDTO dto) {
        ProcurementRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        ProcurementItem entity = mapper.toEntity(dto);
        entity.setProcurementRequest(request);

        ProcurementItem saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    public List<ProcurementItemDTO> getItemsByRequestId(UUID requestId) {
        ProcurementRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        return request.getItems().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}

