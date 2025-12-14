package com.tugas_akhir.procurement_service.domain.delivery.service;

import com.tugas_akhir.procurement_service.domain.procurementrequest.dto.ProcurementRequestDTOs;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.tugas_akhir.procurement_service.domain.delivery.dto.DeliveryDetailDTO;
import com.tugas_akhir.procurement_service.domain.delivery.entity.DeliveryDetail;
import com.tugas_akhir.procurement_service.domain.delivery.repository.DeliveryDetailRepository;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;
import com.tugas_akhir.procurement_service.domain.procurementrequest.repository.ProcurementRequestRepository;
import com.tugas_akhir.procurement_service.mapper.ProcurementMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryDetailRepository repository;
    private final ProcurementRequestRepository requestRepository;
    private final ProcurementMapper mapper;

    @Transactional
    public ProcurementRequestDTOs.DeliveryDetailDTO setDeliveryDetail(UUID requestId, ProcurementRequestDTOs.DeliveryDetailDTO dto) {
        ProcurementRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        DeliveryDetail entity = mapper.toEntity(dto);
        entity.setProcurementRequest(request);

        DeliveryDetail saved = repository.save(entity);
        return mapper.toDTO(saved);
    }
}

