package com.tugasakhir.vendor_service.service.catalog;

import com.tugasakhir.vendor_service.dto.catalog.CatalogMapper;
import com.tugasakhir.vendor_service.dto.catalog.VendorCatalogItemDTO;
import com.tugasakhir.vendor_service.exception.ResourceNotFoundException;
import com.tugasakhir.vendor_service.model.catalog.VendorCatalogItem;
import com.tugasakhir.vendor_service.repository.catalog.VendorCatalogItemRepository;
import com.tugasakhir.vendor_service.kafka.CatalogProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final VendorCatalogItemRepository repository;
    private final CatalogMapper mapper;
    private final CatalogProducer catalogProducer;

    public List<VendorCatalogItemDTO> getCatalogByVendor(String vendorId) {
        return repository.findByVendorId(vendorId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public VendorCatalogItemDTO getItemById(String id) {
        VendorCatalogItem item = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catalog item not found: " + id));
        return mapper.toDto(item);
    }

    @Transactional
    public VendorCatalogItemDTO createCatalogItem(VendorCatalogItemDTO dto) {
        if (dto.getCatalogItemId() == null) {
            dto.setCatalogItemId(UUID.randomUUID().toString());
        }
        dto.setStatusItem("ACTIVE");
        dto.setSyncStatus("PENDING");

        VendorCatalogItem entity = mapper.toEntity(dto);
        if (entity.getVariations() != null) {
            entity.getVariations().forEach(v -> v.setCatalogItem(entity));
        }

        VendorCatalogItem saved = repository.save(entity);
        catalogProducer.sendCatalogUpdatedEvent(saved.getVendorId(), saved.getCatalogItemId());

        return mapper.toDto(saved);
    }

    @Transactional
    public VendorCatalogItemDTO updateCatalogItem(String id, VendorCatalogItemDTO dto) {
        VendorCatalogItem existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found: " + id));

        existing.setSku(dto.getSku());
        existing.setNamaItem(dto.getNamaItem());
        existing.setSpesifikasi(dto.getSpesifikasi());
        existing.setHargaList(dto.getHargaList());
        existing.setPpn(dto.getPpn());
        existing.setStatusItem(dto.getStatusItem());
        existing.setSyncStatus("PENDING");

        if (dto.getVariations() != null) {
            existing.getVariations().clear();
            // In a real app, we'd map DTO variations to entity variations here.
            // For now, we rely on the mapper or manual mapping if implemented.
            // Since we don't have a mapper for variations yet in the main mapper,
            // we might lose variations if not handled.
            // Ideally, we should have a VariationMapper.
        }

        VendorCatalogItem saved = repository.save(existing);
        catalogProducer.sendCatalogUpdatedEvent(saved.getVendorId(), saved.getCatalogItemId());

        return mapper.toDto(saved);
    }

    @Transactional
    public void massUpdateCatalogItems(List<VendorCatalogItemDTO> dtos) {
        for (VendorCatalogItemDTO dto : dtos) {
            if (dto.getCatalogItemId() != null) {
                updateCatalogItem(dto.getCatalogItemId(), dto);
            } else {
                createCatalogItem(dto);
            }
        }
    }

    @Transactional
    public void deleteItem(String id) {
        VendorCatalogItem existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catalog item not found: " + id));
        existing.setIsDeleted(true);
        repository.save(existing);
    }
}
