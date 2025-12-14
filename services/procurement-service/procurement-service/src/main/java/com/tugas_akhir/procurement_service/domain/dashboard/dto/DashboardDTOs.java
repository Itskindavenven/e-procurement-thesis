package com.tugas_akhir.procurement_service.domain.dashboard.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * DTOs for dashboard and reporting
 */
public class DashboardDTOs {

    /**
     * Operator dashboard summary
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperatorDashboardDTO {
        // PR Statistics
        private Integer totalPRs;
        private Integer draftPRs;
        private Integer submittedPRs;
        private Integer approvedPRs;
        private Integer rejectedPRs;

        // Financial
        private BigDecimal totalValue;
        private BigDecimal pendingValue;

        // Recent Activity
        private Integer prsThisMonth;
        private Integer prsThisWeek;

        // Alerts
        private Integer escalatedPRs;
        private Integer returnedPRs;

        private LocalDateTime generatedAt;
    }

    /**
     * Supervisor dashboard summary
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SupervisorDashboardDTO {
        // Pending Approvals
        private Integer pendingApprovals;
        private Integer escalatedApprovals;
        private Integer urgentApprovals;

        // Approval Statistics
        private Integer approvedToday;
        private Integer approvedThisWeek;
        private Integer approvedThisMonth;
        private Integer rejectedThisMonth;

        // Performance Metrics
        private Double averageApprovalTimeHours;
        private Integer oldestPendingDays;

        // Financial
        private BigDecimal pendingApprovalValue;
        private BigDecimal approvedValueThisMonth;

        private LocalDateTime generatedAt;
    }

    /**
     * PR status breakdown
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PRStatusBreakdownDTO {
        private Map<String, Integer> statusCounts; // status -> count
        private Map<String, BigDecimal> statusValues; // status -> total value
    }

    /**
     * Monthly PR statistics
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyPRStatsDTO {
        private Integer year;
        private Integer month;
        private Integer totalPRs;
        private Integer approvedPRs;
        private Integer rejectedPRs;
        private BigDecimal totalValue;
        private BigDecimal approvedValue;
    }

    /**
     * Vendor performance summary
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VendorPerformanceDTO {
        private UUID vendorId;
        private String vendorName;
        private Integer totalPOs;
        private Integer completedPOs;
        private Integer pendingPOs;
        private BigDecimal totalValue;
        private Double onTimeDeliveryRate;
        private Double qualityAcceptanceRate;
    }

    /**
     * Receiving statistics
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceivingStatsDTO {
        private Integer totalReceivings;
        private Integer accepted;
        private Integer partiallyAccepted;
        private Integer rejected;
        private Integer returned;
        private Double acceptanceRate;
    }

    /**
     * Service termin statistics
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TerminStatsDTO {
        private Integer totalTermins;
        private Integer pending;
        private Integer reviewed;
        private Integer approved;
        private Integer clarificationRequested;
        private Integer revisionRequested;
        private BigDecimal totalValue;
        private BigDecimal approvedValue;
    }
}
