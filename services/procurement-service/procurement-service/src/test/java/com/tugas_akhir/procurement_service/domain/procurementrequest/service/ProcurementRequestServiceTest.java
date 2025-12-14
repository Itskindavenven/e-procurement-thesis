package com.tugas_akhir.procurement_service.domain.procurementrequest.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.tugas_akhir.procurement_service.common.enums.ProcurementStatus;
import com.tugas_akhir.procurement_service.domain.procurementrequest.dto.ProcurementRequestDTOs.*;
import com.tugas_akhir.procurement_service.domain.procurementrequest.entity.ProcurementRequest;
import com.tugas_akhir.procurement_service.domain.procurementrequest.mapper.ProcurementRequestMapper;
import com.tugas_akhir.procurement_service.domain.procurementrequest.repository.ProcurementRequestRepository;
import com.tugas_akhir.procurement_service.event.producer.ProcurementEventProducer;
import com.tugas_akhir.procurement_service.exception.ResourceNotFoundException;
import com.tugas_akhir.procurement_service.exception.ValidationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

/**
 * Unit tests for ProcurementRequestService
 */
@ExtendWith(MockitoExtension.class)
class ProcurementRequestServiceTest {

    @Mock
    private ProcurementRequestRepository procurementRequestRepository;

    @Mock
    private ProcurementRequestMapper mapper;

    @Mock
    private ProcurementEventProducer eventProducer;

    @InjectMocks
    private ProcurementRequestService procurementRequestService;

    private UUID operatorId;
    private UUID prId;
    private ProcurementRequest draftPR;

    @BeforeEach
    void setUp() {
        operatorId = UUID.randomUUID();
        prId = UUID.randomUUID();

        draftPR = ProcurementRequest.builder()
                .id(prId)
                .operatorId(operatorId)
                .description("Test PR")
                .status(ProcurementStatus.DRAFT)
                .build();
    }

    @Test
    void submitForApproval_Success() {
        // Given
        when(procurementRequestRepository.findById(prId)).thenReturn(Optional.of(draftPR));
        when(procurementRequestRepository.save(any(ProcurementRequest.class))).thenReturn(draftPR);
        when(mapper.toResponseDTO(any())).thenReturn(new ProcurementRequestResponseDTO());

        // When
        ProcurementRequestResponseDTO result = procurementRequestService.submitForApproval(prId, operatorId);

        // Then
        assertNotNull(result);
        verify(procurementRequestRepository).save(any(ProcurementRequest.class));
        verify(eventProducer, times(1)).publishPRSubmitted(any());
    }

    @Test
    void submitForApproval_PRNotFound_ThrowsException() {
        // Given
        when(procurementRequestRepository.findById(prId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> procurementRequestService.submitForApproval(prId, operatorId));
    }

    @Test
    void submitForApproval_NotOwner_ThrowsException() {
        // Given
        UUID differentOperator = UUID.randomUUID();
        when(procurementRequestRepository.findById(prId)).thenReturn(Optional.of(draftPR));

        // When & Then
        assertThrows(ValidationException.class,
                () -> procurementRequestService.submitForApproval(prId, differentOperator));
    }

    @Test
    void deleteProcurementRequest_OnlyDraftsCanBeDeleted() {
        // Given
        ProcurementRequest submittedPR = ProcurementRequest.builder()
                .id(prId)
                .operatorId(operatorId)
                .status(ProcurementStatus.SUBMITTED)
                .build();
        when(procurementRequestRepository.findById(prId)).thenReturn(Optional.of(submittedPR));

        // When & Then
        assertThrows(ValidationException.class,
                () -> procurementRequestService.deleteProcurementRequest(prId, operatorId));
    }
}
