package com.tugasakhir.vendor_service.service.delivery;

import com.tugasakhir.vendor_service.dto.delivery.DeliveryDTO;
import com.tugasakhir.vendor_service.dto.delivery.DeliveryMapper;
import com.tugasakhir.vendor_service.exception.ResourceNotFoundException;
import com.tugasakhir.vendor_service.external.LogisticsClient;
import com.tugasakhir.vendor_service.model.delivery.Delivery;
import com.tugasakhir.vendor_service.repository.delivery.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final DeliveryRepository repository;
    private final DeliveryMapper mapper;
    private final LogisticsClient logisticsClient;

    @Transactional
    public DeliveryDTO createDelivery(DeliveryDTO dto) {
        if (dto.getDeliveryId() == null) {
            dto.setDeliveryId(UUID.randomUUID().toString());
        }
        dto.setStatusDelivery("PENDING");

        // Call Logistics Service to get tracking number
        try {
            String trackingNumber = logisticsClient.createShipment(dto.getPoId(), "Vendor Address"); // Simplified
            dto.setTrackingNumber(trackingNumber);
        } catch (Exception e) {
            log.error("Failed to create shipment in Logistics Service", e);
            // Decide: Fail or continue? Let's continue with null tracking for now or throw.
            // For robustness, we might set status to ERROR_LOGISTICS.
        }

        Delivery entity = mapper.toEntity(dto);
        if (entity.getPackingLists() != null) {
            entity.getPackingLists().forEach(pl -> pl.setDelivery(entity));
        }

        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public void updateDeliveryStatus(String deliveryId, String status) {
        Delivery delivery = repository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found: " + deliveryId));
        delivery.setStatusDelivery(status);
        repository.save(delivery);
        log.info("Updated delivery {} status to {}", deliveryId, status);
    }

    public void uploadPackingList(String deliveryId, MultipartFile file) {
        // Mock upload logic
        log.info("Uploaded packing list for delivery {}: {}", deliveryId, file.getOriginalFilename());
        // In real app, save file to storage and update Delivery entity with URL
    }

    public void uploadPOD(String deliveryId, MultipartFile file) {
        // Mock upload logic
        log.info("Uploaded POD for delivery {}: {}", deliveryId, file.getOriginalFilename());
        // In real app, save file to storage and update Delivery entity with URL
    }
}
