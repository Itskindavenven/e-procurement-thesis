package com.tugas_akhir.procurement_service.domain.serviceProgress.dto;

import com.tugas_akhir.procurement_service.common.enums.ServiceProgressStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ServiceProgressDTOs {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServiceProgressDTO {
        private UUID id;
        private UUID terminId;
        private BigDecimal progressPercentage;
        private String vendorReportUrl;
        private String supportingDocsUrl;
        private LocalDateTime timestamp;
        private ServiceProgressStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateServiceProgressRequest {
        private UUID terminId;
        private BigDecimal progressPercentage;
        private String vendorReportUrl;
        private String supportingDocsUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateServiceProgressRequest {
        private BigDecimal progressPercentage;
        private String vendorReportUrl;
        private String supportingDocsUrl;
        private ServiceProgressStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServiceProgressResponseDTO {
        private UUID id;
        private UUID terminId;
        private String terminName;
        private BigDecimal progressPercentage;
        private String vendorReportUrl;
        private String supportingDocsUrl;
        private LocalDateTime timestamp;
        private ServiceProgressStatus status;
    }
}
