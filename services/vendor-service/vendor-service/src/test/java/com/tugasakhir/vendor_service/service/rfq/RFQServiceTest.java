package com.tugasakhir.vendor_service.service.rfq;

import com.tugasakhir.vendor_service.dto.rfq.RFQDTO;
import com.tugasakhir.vendor_service.dto.rfq.RFQMapper;
import com.tugasakhir.vendor_service.exception.ResourceNotFoundException;
import com.tugasakhir.vendor_service.kafka.RFQProducer;
import com.tugasakhir.vendor_service.model.rfq.RFQ;
import com.tugasakhir.vendor_service.repository.rfq.RFQRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RFQServiceTest {

    @Mock
    private RFQRepository repository;

    @Mock
    private RFQMapper mapper;

    @Mock
    private RFQProducer rfqProducer;

    @Mock
    private com.tugasakhir.vendor_service.repository.rfq.ClarificationRepository clarificationRepository;

    @InjectMocks
    private RFQService service;

    private RFQ rfq;
    private RFQDTO rfqDTO;

    @BeforeEach
    void setUp() {
        rfq = new RFQ();
        rfq.setRfqId(UUID.randomUUID().toString());
        rfq.setStatusRfq("OPEN");
        rfq.setTenggatRespon(java.time.LocalDateTime.now().plusDays(5));

        rfqDTO = new RFQDTO();
        rfqDTO.setRfqId(rfq.getRfqId());
        rfqDTO.setStatusRfq(rfq.getStatusRfq());
    }

    @Test
    void getOpenRFQsForVendor_Success() {
        when(repository.findByStatusRfq("OPEN"))
                .thenReturn(Collections.singletonList(rfq));
        when(mapper.toDto(rfq)).thenReturn(rfqDTO);

        List<RFQDTO> result = service.getOpenRFQsForVendor("vendor-1");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(rfq.getRfqId(), result.get(0).getRfqId());
    }

    @Test
    void getRFQById_Success() {
        when(repository.findById(rfq.getRfqId())).thenReturn(Optional.of(rfq));
        when(mapper.toDto(rfq)).thenReturn(rfqDTO);

        RFQDTO result = service.getRFQById(rfq.getRfqId());

        assertNotNull(result);
        assertEquals(rfq.getRfqId(), result.getRfqId());
    }

    @Test
    void declineRFQ_Success() {
        when(repository.findById(rfq.getRfqId())).thenReturn(Optional.of(rfq));
        // repository.save is called but void return type in service usually ignores
        // return unless captured
        // but here we mock it to return rfq
        when(repository.save(any(RFQ.class))).thenReturn(rfq);

        service.declineRFQ(rfq.getRfqId(), "vendor-1", "Too expensive");

        verify(repository, times(1)).save(any(RFQ.class));
        verify(rfqProducer, times(1)).sendRFQDeclinedEvent(eq(rfq.getRfqId()), eq("vendor-1"), eq("Too expensive"));
    }
}
