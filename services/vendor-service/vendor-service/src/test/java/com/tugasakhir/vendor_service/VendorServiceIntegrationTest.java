package com.tugasakhir.vendor_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tugasakhir.vendor_service.dto.vendor.VendorAccountDTO;
import com.tugasakhir.vendor_service.repository.vendor.VendorAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class VendorServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VendorAccountRepository vendorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        vendorRepository.deleteAll();
    }

    @Test
    void createAndGetVendor_Success() throws Exception {
        VendorAccountDTO vendor = new VendorAccountDTO();
        vendor.setNamaPerusahaan("Integration Vendor");
        vendor.setEmailKontak("integration@vendor.com");
        // Address and Phone are not in DTO currently

        // Create Vendor
        String responseContent = mockMvc.perform(post("/api/vendor/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vendor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.namaPerusahaan").value("Integration Vendor"))
                .andReturn().getResponse().getContentAsString();

        VendorAccountDTO createdVendor = objectMapper.readValue(responseContent, VendorAccountDTO.class);

        // Get Vendor
        mockMvc.perform(get("/api/vendor/account/" + createdVendor.getVendorId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailKontak").value("integration@vendor.com"));
    }

    @Test
    void swaggerUi_ShouldBeAccessible() throws Exception {
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection()); // Usually redirects to /swagger-ui/index.html

        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }
}
