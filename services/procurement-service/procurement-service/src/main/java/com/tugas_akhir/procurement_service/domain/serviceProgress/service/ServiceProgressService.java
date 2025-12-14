package com.tugas_akhir.procurement_service.domain.serviceProgress.service;

import com.tugas_akhir.procurement_service.mapper.ProcurementMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.tugas_akhir.procurement_service.domain.serviceProgress.dto.ServiceProgressDTOs.ServiceProgressDTO;
import com.tugas_akhir.procurement_service.domain.serviceProgress.entity.ServiceProgress;
import com.tugas_akhir.procurement_service.common.enums.ServiceProgressStatus;
import com.tugas_akhir.procurement_service.domain.serviceProgress.repository.ServiceProgressRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceProgressService {

    private final ServiceProgressRepository repository;
    private final ProcurementMapper mapper;

    @Transactional
    public ServiceProgressDTO confirmServiceCompletion(UUID progressId) {
        ServiceProgress entity = repository.findById(progressId)
                .orElseThrow(() -> new RuntimeException("Progress not found"));

        entity.setStatus(ServiceProgressStatus.VERIFIED);
        // Logic to notify finance or update termin status
        ServiceProgress saved = repository.save(entity);
        return mapper.toDTO(saved);
    }
}
