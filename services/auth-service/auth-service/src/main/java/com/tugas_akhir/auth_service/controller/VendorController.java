package com.tugas_akhir.auth_service.controller;

import com.tugas_akhir.auth_service.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/vendor")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestParam("companyName") String companyName,
            @RequestParam("npwp") String npwp,
            @RequestParam("address") String address,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam(value = "documents", required = false) MultipartFile[] documents) {
        vendorService.registerVendor(companyName, npwp, address, email, phone, documents);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
