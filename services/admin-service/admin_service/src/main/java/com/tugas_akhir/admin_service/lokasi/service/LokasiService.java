package com.tugas_akhir.admin_service.lokasi.service;

import com.tugas_akhir.admin_service.lokasi.dto.LokasiDTO;
import com.tugas_akhir.admin_service.lokasi.entity.Lokasi;
import com.tugas_akhir.admin_service.lokasi.entity.OperatorLocation;
import com.tugas_akhir.admin_service.lokasi.entity.SupervisorLocation;
import com.tugas_akhir.admin_service.lokasi.mapper.LokasiMapper;
import com.tugas_akhir.admin_service.lokasi.repository.LokasiRepository;
import com.tugas_akhir.admin_service.lokasi.repository.OperatorLocationRepository;
import com.tugas_akhir.admin_service.lokasi.repository.SupervisorLocationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LokasiService {

    private final LokasiRepository lokasiRepository;
    private final OperatorLocationRepository operatorLocationRepository;
    private final SupervisorLocationRepository supervisorLocationRepository;
    private final LokasiMapper lokasiMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional(readOnly = true)
    public List<LokasiDTO> getAllLocations() {
        return lokasiRepository.findAll().stream()
                .map(lokasiMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LokasiDTO getLocationById(UUID id) {
        Lokasi lokasi = lokasiRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with ID: " + id));
        return lokasiMapper.toDTO(lokasi);
    }

    @Transactional
    public LokasiDTO createLocation(LokasiDTO dto) {
        if (lokasiRepository.existsByLocationName(dto.getLocationName())) {
            throw new IllegalArgumentException("Location name already exists: " + dto.getLocationName());
        }

        Lokasi lokasi = lokasiMapper.toEntity(dto);
        lokasi.setStatus("ACTIVE");
        Lokasi savedLokasi = lokasiRepository.save(lokasi);

        // Publish event
        kafkaTemplate.send("admin.location.events", "location.created", savedLokasi.getLocationId().toString());

        return lokasiMapper.toDTO(savedLokasi);
    }

    @Transactional
    public LokasiDTO updateLocation(UUID id, LokasiDTO dto) {
        Lokasi lokasi = lokasiRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with ID: " + id));

        if (!lokasi.getLocationName().equals(dto.getLocationName())
                && lokasiRepository.existsByLocationName(dto.getLocationName())) {
            throw new IllegalArgumentException("Location name already exists: " + dto.getLocationName());
        }

        lokasiMapper.updateEntityFromDTO(dto, lokasi);
        Lokasi updatedLokasi = lokasiRepository.save(lokasi);

        // Publish event
        kafkaTemplate.send("admin.location.events", "location.updated", updatedLokasi.getLocationId().toString());

        return lokasiMapper.toDTO(updatedLokasi);
    }

    @Transactional
    public void deactivateLocation(UUID id) {
        Lokasi lokasi = lokasiRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with ID: " + id));

        lokasi.setStatus("INACTIVE");
        lokasi.setDeleted(true);
        lokasiRepository.save(lokasi);

        // Publish event
        kafkaTemplate.send("admin.location.events", "location.deactivated", id.toString());
    }

    @Transactional
    public void assignOperator(UUID locationId, UUID operatorId) {
        if (!lokasiRepository.existsById(locationId)) {
            throw new EntityNotFoundException("Location not found with ID: " + locationId);
        }

        if (operatorLocationRepository.existsByOperatorId(operatorId)) {
            throw new IllegalArgumentException("Operator is already assigned to a location.");
        }

        OperatorLocation assignment = new OperatorLocation();
        assignment.setLocationId(locationId);
        assignment.setOperatorId(operatorId);
        assignment.setAssignedAt(LocalDateTime.now());

        operatorLocationRepository.save(assignment);

        kafkaTemplate.send("admin.location.events", "operator.assigned", operatorId.toString());
    }

    @Transactional
    public void assignSupervisor(UUID locationId, UUID supervisorId) {
        if (!lokasiRepository.existsById(locationId)) {
            throw new EntityNotFoundException("Location not found with ID: " + locationId);
        }

        // Supervisor can have multiple locations, so no check for existing assignment

        SupervisorLocation assignment = new SupervisorLocation();
        assignment.setLocationId(locationId);
        assignment.setSupervisorId(supervisorId);
        assignment.setAssignedAt(LocalDateTime.now());

        supervisorLocationRepository.save(assignment);

        kafkaTemplate.send("admin.location.events", "supervisor.assigned", supervisorId.toString());
    }
}
