package com.tugasakhir.vendor_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tugasakhir.vendor_service.service.vendor.VendorAccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AdminEventConsumerUnitTest {

    @Mock
    private VendorAccountService vendorAccountService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AdminEventConsumer consumer;

    @Test
    void listen_VendorApproved_UpdatesStatusToActive() {
        String key = "vendor.approved";
        String vendorId = "vendor-123";

        consumer.listen(key, vendorId);

        verify(vendorAccountService).updateVendorStatus(vendorId, "ACTIVE");
    }

    @Test
    void listen_VendorRejected_UpdatesStatusToRejected() {
        String key = "vendor.rejected";
        String vendorId = "vendor-123";

        consumer.listen(key, vendorId);

        verify(vendorAccountService).updateVendorStatus(vendorId, "REJECTED");
    }

    @Test
    void listen_RevisionRequested_UpdatesStatusToRevisionRequested() {
        String key = "vendor.revision.requested";
        String vendorId = "vendor-123";

        consumer.listen(key, vendorId);

        verify(vendorAccountService).updateVendorStatus(vendorId, "REVISION_REQUESTED");
    }

    @Test
    void listen_UnknownEvent_DoesNothing() {
        String key = "unknown.event";
        String vendorId = "vendor-123";

        consumer.listen(key, vendorId);

        verify(vendorAccountService, never()).updateVendorStatus(any(), any());
    }
}
