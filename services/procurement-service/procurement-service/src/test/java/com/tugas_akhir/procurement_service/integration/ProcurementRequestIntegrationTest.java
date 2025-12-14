package com.tugas_akhir.procurement_service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tugas_akhir.procurement_service.domain.procurementrequest.dto.ProcurementRequestDTO;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;
import com.tugas_akhir.procurement_service.domain.procurementrequest.repository.ProcurementRequestRepository;
import com.tugas_akhir.procurement_service.common.enums.ProcurementStatus;
import com.tugas_akhir.procurement_service.AbstractIntegrationTest;
import com.tugas_akhir.procurement_service.TestDataFactory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class ProcurementRequestIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProcurementRequestRepository repository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSubmitProcurementRequest_andChangeStatus() throws Exception {
        // Arrange: Save a draft first
        ProcurementRequest draft = TestDataFactory.createProcurementRequest();
        draft.setStatus(ProcurementStatus.DRAFT);
        draft = repository.save(draft);

        // Act & Assert
        mockMvc.perform(post("/api/v1/procurement-requests/" + draft.getId() + "/submit")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("SUBMITTED"));

        // Verify DB
        ProcurementRequest updated = repository.findById(draft.getId()).orElseThrow();
        assert updated.getStatus() == ProcurementStatus.SUBMITTED;
    }
}
