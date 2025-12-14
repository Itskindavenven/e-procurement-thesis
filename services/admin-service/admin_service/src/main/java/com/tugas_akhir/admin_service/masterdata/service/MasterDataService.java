package com.tugas_akhir.admin_service.masterdata.service;

import com.tugas_akhir.admin_service.masterdata.dto.MasterDataDTO;
import com.tugas_akhir.admin_service.masterdata.entity.MasterDataItem;
import com.tugas_akhir.admin_service.masterdata.entity.MasterDataRelation;
import com.tugas_akhir.admin_service.masterdata.mapper.MasterDataMapper;
import com.tugas_akhir.admin_service.masterdata.repository.MasterDataRelationRepository;
import com.tugas_akhir.admin_service.masterdata.repository.MasterDataRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MasterDataService {

    private final MasterDataRepository masterDataRepository;
    private final MasterDataRelationRepository masterDataRelationRepository;
    private final MasterDataMapper masterDataMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional(readOnly = true)
    public List<MasterDataDTO> getAllMasterData() {
        return masterDataRepository.findAll().stream()
                .map(masterDataMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MasterDataDTO getMasterDataById(UUID id) {
        MasterDataItem item = masterDataRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Master Data not found with ID: " + id));
        return masterDataMapper.toDTO(item);
    }

    @Transactional
    public MasterDataDTO createMasterData(MasterDataDTO dto) {
        if (dto.getCode() != null && masterDataRepository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Code already exists: " + dto.getCode());
        }

        MasterDataItem item = masterDataMapper.toEntity(dto);
        item.setStatus("ACTIVE");
        MasterDataItem savedItem = masterDataRepository.save(item);

        // Publish event
        kafkaTemplate.send("admin.masterdata.events", "masterdata.created", savedItem.getItemId().toString());

        return masterDataMapper.toDTO(savedItem);
    }

    @Transactional
    public MasterDataDTO updateMasterData(UUID id, MasterDataDTO dto) {
        MasterDataItem item = masterDataRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Master Data not found with ID: " + id));

        if (dto.getCode() != null && !dto.getCode().equals(item.getCode())
                && masterDataRepository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Code already exists: " + dto.getCode());
        }

        masterDataMapper.updateEntityFromDTO(dto, item);
        MasterDataItem updatedItem = masterDataRepository.save(item);

        // Publish event
        kafkaTemplate.send("admin.masterdata.events", "masterdata.updated", updatedItem.getItemId().toString());

        return masterDataMapper.toDTO(updatedItem);
    }

    @Transactional
    public void deleteMasterData(UUID id) {
        MasterDataItem item = masterDataRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Master Data not found with ID: " + id));

        item.setDeleted(true);
        item.setStatus("DELETED");
        masterDataRepository.save(item);

        kafkaTemplate.send("admin.masterdata.events", "masterdata.deleted", id.toString());
    }

    @Transactional
    public void createRelation(UUID parentId, UUID childId, String relationType) {
        if (!masterDataRepository.existsById(parentId)) {
            throw new EntityNotFoundException("Parent Master Data not found with ID: " + parentId);
        }
        if (!masterDataRepository.existsById(childId)) {
            throw new EntityNotFoundException("Child Master Data not found with ID: " + childId);
        }

        MasterDataRelation relation = new MasterDataRelation();
        relation.setParentId(parentId);
        relation.setChildId(childId);
        relation.setRelationType(relationType);

        masterDataRelationRepository.save(relation);

        kafkaTemplate.send("admin.masterdata.relation.updated", "masterdata.relation.created",
                relation.getRelationId().toString());
    }

    @Transactional
    public void syncMasterData(String targetService) {
        // Logic to trigger sync to other services
        // This might involve sending a bulk message or calling an API
        kafkaTemplate.send("admin.masterdata.events", "sync.requested", targetService);
    }
}
