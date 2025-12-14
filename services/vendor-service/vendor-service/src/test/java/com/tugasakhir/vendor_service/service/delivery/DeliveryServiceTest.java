package com.tugasakhir.vendor_service.service.delivery;

import com.tugasakhir.vendor_service.dto.delivery.DeliveryDTO;
import com.tugasakhir.vendor_service.dto.delivery.DeliveryMapper;
import com.tugasakhir.vendor_service.external.LogisticsClient;
import com.tugasakhir.vendor_service.model.delivery.Delivery;
import com.tugasakhir.vendor_service.repository.delivery.DeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private DeliveryRepository repository;

    @Mock
    private DeliveryMapper mapper;

    @Mock
    private LogisticsClient logisticsClient;

    @InjectMocks
    private DeliveryService service;

    private Delivery delivery;
    private DeliveryDTO deliveryDTO;

    @BeforeEach
    void setUp() {
        delivery = new Delivery();
        delivery.setDeliveryId(UUID.randomUUID().toString());
        delivery.setPoId("PO-123");

        deliveryDTO = new DeliveryDTO();
        deliveryDTO.setDeliveryId(delivery.getDeliveryId());
        deliveryDTO.setPoId(delivery.getPoId());
    }

    @Test
    void createDelivery_Success() {
        when(logisticsClient.createShipment(anyString(), anyString())).thenReturn("TRACK-123");
        when(mapper.toEntity(any(DeliveryDTO.class))).thenReturn(delivery);
        when(repository.save(any(Delivery.class))).thenReturn(delivery);
        when(mapper.toDto(any(Delivery.class))).thenReturn(deliveryDTO);

        DeliveryDTO result = service.createDelivery(deliveryDTO);

        assertNotNull(result);
        verify(logisticsClient, times(1)).createShipment(anyString(), anyString());
        verify(repository, times(1)).save(any(Delivery.class));
    }
}
