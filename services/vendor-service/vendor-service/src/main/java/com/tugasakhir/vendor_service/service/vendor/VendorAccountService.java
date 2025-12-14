package com.tugasakhir.vendor_service.service.vendor;

import com.tugasakhir.vendor_service.dto.vendor.VendorAccountDTO;
import com.tugasakhir.vendor_service.dto.vendor.VendorAccountMapper;
import com.tugasakhir.vendor_service.exception.ResourceNotFoundException;
import com.tugasakhir.vendor_service.model.vendor.VendorAccount;
import com.tugasakhir.vendor_service.repository.vendor.VendorAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorAccountService {

    private final VendorAccountRepository repository;
    private final VendorAccountMapper mapper;

    public List<VendorAccountDTO> getAllVendors() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public VendorAccountDTO getVendorById(String id) {
        VendorAccount vendor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + id));
        return mapper.toDto(vendor);
    }

    @Transactional
    public VendorAccountDTO createVendor(VendorAccountDTO dto) {
        VendorAccount entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public VendorAccountDTO updateVendor(String id, VendorAccountDTO dto) {
        VendorAccount existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + id));

        existing.setNamaPerusahaan(dto.getNamaPerusahaan());
        existing.setEmailKontak(dto.getEmailKontak());
        existing.setStatus(dto.getStatus());
        existing.setRatingPerforma(dto.getRatingPerforma());

        return mapper.toDto(repository.save(existing));
    }

    @Transactional
    public void syncVendorFromUser(com.tugasakhir.vendor_service.dto.kafka.UserEventDto event) {
        // Check if user has ROLE_VENDOR
        boolean isVendor = event.getRoles() != null && event.getRoles().stream()
                .anyMatch(r -> "ROLE_VENDOR".equals(r.getName()));

        if (!isVendor) {
            return;
        }

        VendorAccount vendor = repository.findById(event.getUserId())
                .orElse(new VendorAccount());

        if (vendor.getVendorId() == null) {
            vendor.setVendorId(event.getUserId());
            vendor.setNamaPerusahaan(event.getUsername()); // Default to username/email initially
            vendor.setRatingPerforma(0.0);
        }

        vendor.setEmailKontak(event.getEmail());

        // Map Auth Status to Vendor Status if needed, or just keep it simple
        // For now, we assume active if user is active
        if ("ACTIVE".equals(event.getStatus())) {
            vendor.setStatus("ACTIVE");
        } else {
            vendor.setStatus("INACTIVE");
        }

        repository.save(vendor);
    }

    @Transactional
    public void updateVendorStatus(String vendorId, String status) {
        VendorAccount vendor = repository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + vendorId));

        vendor.setStatus(status);
        repository.save(vendor);
    }
}
