package com.tugas_akhir.auth_service.kafka;

import com.tugas_akhir.auth_service.domain.User;
import com.tugas_akhir.auth_service.service.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AdminEventConsumerUnitTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminEventConsumer consumer;

    @Test
    void listen_VendorApproved_UpdatesStatusToActive() {
        String key = "vendor.approved";
        String userId = UUID.randomUUID().toString();

        consumer.listen(key, userId);

        verify(adminService).updateUserStatus(UUID.fromString(userId), User.UserStatus.ACTIVE);
    }

    @Test
    void listen_VendorRejected_UpdatesStatusToRejected() {
        String key = "vendor.rejected";
        String userId = UUID.randomUUID().toString();

        consumer.listen(key, userId);

        verify(adminService).updateUserStatus(UUID.fromString(userId), User.UserStatus.REJECTED);
    }

    @Test
    void listen_UnknownEvent_DoesNothing() {
        String key = "unknown.event";
        String userId = UUID.randomUUID().toString();

        consumer.listen(key, userId);

        verify(adminService, never()).updateUserStatus(any(), any());
    }
}
