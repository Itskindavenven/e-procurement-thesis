package com.tugasakhir.vendor_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tugasakhir.vendor_service.dto.kafka.UserEventDto;
import com.tugasakhir.vendor_service.service.vendor.VendorAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class UserEventConsumerUnitTest {

    @Mock
    private VendorAccountService vendorAccountService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private UserEventConsumer consumer;

    @Test
    void listen_UserCreated_CallsSync() throws Exception {
        String json = "{\"eventType\":\"USER_CREATED\"}";
        UserEventDto dto = new UserEventDto();
        dto.setEventType("USER_CREATED");

        org.mockito.Mockito.when(objectMapper.readValue(json, UserEventDto.class)).thenReturn(dto);

        consumer.listen(json);

        verify(vendorAccountService).syncVendorFromUser(dto);
    }

    @Test
    void listen_UserUpdated_CallsSync() throws Exception {
        String json = "{\"eventType\":\"USER_UPDATED\"}";
        UserEventDto dto = new UserEventDto();
        dto.setEventType("USER_UPDATED");

        org.mockito.Mockito.when(objectMapper.readValue(json, UserEventDto.class)).thenReturn(dto);

        consumer.listen(json);

        verify(vendorAccountService).syncVendorFromUser(dto);
    }

    @Test
    void listen_OtherEvent_DoesNotCallSync() throws Exception {
        String json = "{\"eventType\":\"OTHER_EVENT\"}";
        UserEventDto dto = new UserEventDto();
        dto.setEventType("OTHER_EVENT");

        org.mockito.Mockito.when(objectMapper.readValue(json, UserEventDto.class)).thenReturn(dto);

        consumer.listen(json);

        verify(vendorAccountService, never()).syncVendorFromUser(any());
    }

    @Test
    void listen_InvalidJson_LogsError() throws Exception {
        String json = "invalid-json";
        org.mockito.Mockito.when(objectMapper.readValue(json, UserEventDto.class))
                .thenThrow(new RuntimeException("Parse error"));

        consumer.listen(json);

        verify(vendorAccountService, never()).syncVendorFromUser(any());
    }
}
