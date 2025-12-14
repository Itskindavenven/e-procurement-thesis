package com.tugas_akhir.admin_service.vendor.service;

import com.tugas_akhir.admin_service.vendor.dto.VendorDTO;
import com.tugas_akhir.admin_service.vendor.entity.VendorRegistration;
import com.tugas_akhir.admin_service.vendor.mapper.VendorMapper;
import com.tugas_akhir.admin_service.vendor.repository.VendorRepository;
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
public class VendorService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional(readOnly = true)
    public List<VendorDTO> getAllVendorRegistrations() {
        return vendorRepository.findAll().stream()
                .map(vendorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VendorDTO> getPendingVendorRegistrations() {
        return vendorRepository.findByVerificationStatus("PENDING").stream()
                .map(vendorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VendorDTO getVendorRegistrationById(UUID id) {
        VendorRegistration registration = vendorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vendor Registration not found with ID: " + id));
        return vendorMapper.toDTO(registration);
    }

    @Transactional
    public VendorDTO createVendorRegistration(VendorDTO dto) {
        VendorRegistration registration = vendorMapper.toEntity(dto);
        registration.setVerificationStatus("PENDING");
        VendorRegistration savedRegistration = vendorRepository.save(registration);
        return vendorMapper.toDTO(savedRegistration);
    }

    @Transactional
    public VendorDTO verifyVendor(UUID id, String status, String notes) {
        VendorRegistration registration = vendorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vendor Registration not found with ID: " + id));

        if (!"PENDING".equals(registration.getVerificationStatus())
                && !"REVISION_REQUESTED".equals(registration.getVerificationStatus())) {
            // Allow re-verification if it was revision requested, or maybe strict flow?
            // Use case says: Admin chooses action.
        }

        registration.setVerificationStatus(status);
        registration.setAdminNotes(notes);

        VendorRegistration updatedRegistration = vendorRepository.save(registration);

        // Publish event
        String eventType = "VENDOR_" + status; // VENDOR_APPROVED, VENDOR_REJECTED, VENDOR_REVISION_REQUESTED
        kafkaTemplate.send("admin.vendor.events", eventType.toLowerCase().replace("_", "."),
                updatedRegistration.getVendorId().toString());

        return vendorMapper.toDTO(updatedRegistration);
    }
}
