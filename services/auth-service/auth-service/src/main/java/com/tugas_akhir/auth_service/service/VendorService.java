package com.tugas_akhir.auth_service.service;

import com.tugas_akhir.auth_service.domain.Role;
import com.tugas_akhir.auth_service.domain.User;
import com.tugas_akhir.auth_service.domain.VendorProfile;
import com.tugas_akhir.auth_service.integration.AuditPublisher;
import com.tugas_akhir.auth_service.integration.MasterDataPublisher;
import com.tugas_akhir.auth_service.integration.NotificationPublisher;
import com.tugas_akhir.auth_service.repository.RoleRepository;
import com.tugas_akhir.auth_service.repository.UserRepository;
import com.tugas_akhir.auth_service.repository.VendorProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final UserRepository userRepository;
    private final VendorProfileRepository vendorProfileRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditPublisher auditPublisher;
    private final NotificationPublisher notificationPublisher;
    private final MasterDataPublisher masterDataPublisher;

    @Transactional
    public void registerVendor(String companyName, String npwp, String address, String email, String phone,
            MultipartFile[] documents) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        // Create User
        User user = new User();
        user.setUsername(email); // Use email as username for vendors initially
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(UUID.randomUUID().toString())); // Temporary random password
        user.setStatus(User.UserStatus.PENDING_VERIFICATION);

        Role vendorRole = roleRepository.findByName("ROLE_VENDOR")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_VENDOR")));
        user.setRoles(Set.of(vendorRole));

        userRepository.save(user);

        // Upload Stub - Store metadata
        Map<String, Object> docsMeta = new HashMap<>();
        if (documents != null) {
            for (MultipartFile file : documents) {
                docsMeta.put(file.getOriginalFilename(), file.getSize());
            }
        }

        // Create Vendor Profile
        VendorProfile profile = new VendorProfile();
        profile.setUserId(user.getId());
        profile.setCompanyName(companyName);
        profile.setNpwp(npwp);
        profile.setAddress(address);
        profile.setPhone(phone);
        profile.setDocumentsMeta(docsMeta);

        vendorProfileRepository.save(profile);

        // Publish Events
        auditPublisher.publishEvent("VENDOR_REGISTRATION_PENDING", email, "Vendor registration submitted");
        notificationPublisher.sendEmailNotification("vendor_registration", email, Map.of("company", companyName));
        masterDataPublisher.publishUserSyncEvent("USER_CREATED", user);
    }
}
