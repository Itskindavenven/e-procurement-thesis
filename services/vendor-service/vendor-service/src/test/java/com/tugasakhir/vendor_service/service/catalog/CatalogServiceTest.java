package com.tugasakhir.vendor_service.service.catalog;

import com.tugasakhir.vendor_service.dto.catalog.CatalogMapper;
import com.tugasakhir.vendor_service.dto.catalog.VendorCatalogItemDTO;
import com.tugasakhir.vendor_service.exception.ResourceNotFoundException;
import com.tugasakhir.vendor_service.kafka.CatalogProducer;
import com.tugasakhir.vendor_service.model.catalog.VendorCatalogItem;
import com.tugasakhir.vendor_service.repository.catalog.VendorCatalogItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogServiceTest {

    @Mock
    private VendorCatalogItemRepository repository;

    @Mock
    private CatalogMapper mapper;

    @Mock
    private CatalogProducer catalogProducer;

    @InjectMocks
    private CatalogService service;

    private VendorCatalogItem item;
    private VendorCatalogItemDTO itemDTO;

    @BeforeEach
    void setUp() {
        item = new VendorCatalogItem();
        item.setCatalogItemId(UUID.randomUUID().toString());
        item.setVendorId("vendor-1");
        item.setNamaItem("Test Item");

        itemDTO = new VendorCatalogItemDTO();
        itemDTO.setCatalogItemId(item.getCatalogItemId());
        itemDTO.setNamaItem(item.getNamaItem());
    }

    @Test
    void createCatalogItem_Success() {
        when(mapper.toEntity(any(VendorCatalogItemDTO.class))).thenReturn(item);
        when(repository.save(any(VendorCatalogItem.class))).thenReturn(item);
        when(mapper.toDto(any(VendorCatalogItem.class))).thenReturn(itemDTO);

        VendorCatalogItemDTO result = service.createCatalogItem(itemDTO);

        assertNotNull(result);
        assertEquals(itemDTO.getNamaItem(), result.getNamaItem());
        verify(catalogProducer, times(1)).sendCatalogUpdatedEvent(any(), any());
    }

    @Test
    void updateCatalogItem_Success() {
        when(repository.findById(item.getCatalogItemId())).thenReturn(Optional.of(item));
        when(repository.save(any(VendorCatalogItem.class))).thenReturn(item);
        when(mapper.toDto(any(VendorCatalogItem.class))).thenReturn(itemDTO);

        VendorCatalogItemDTO result = service.updateCatalogItem(item.getCatalogItemId(), itemDTO);

        assertNotNull(result);
        verify(catalogProducer, times(1)).sendCatalogUpdatedEvent(any(), any());
    }

    @Test
    void updateCatalogItem_NotFound() {
        String id = "non-existent";
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateCatalogItem(id, itemDTO));
    }
}
