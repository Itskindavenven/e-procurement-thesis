package com.tugas_akhir.procurement_service.domain.dashboard.service;

import com.tugas_akhir.procurement_service.common.enums.ProcurementStatus;
import com.tugas_akhir.procurement_service.domain.dashboard.dto.DashboardDTOs.*;
import com.tugas_akhir.procurement_service.domain.procurementrequest.repository.ProcurementRequestRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Service for operator dashboard data
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardOperatorService {

        private final ProcurementRequestRepository procurementRequestRepository;

        /**
         * Get operator dashboard summary
         */
        @Transactional(readOnly = true)
        public OperatorDashboardDTO getOperatorDashboard(UUID operatorId) {
                log.info("Generating dashboard for operator: {}", operatorId);

                // Count PRs by status
                long totalPRs = procurementRequestRepository.countByOperatorId(operatorId);
                long draftPRs = procurementRequestRepository.countByOperatorIdAndStatus(operatorId,
                                ProcurementStatus.DRAFT);
                long submittedPRs = procurementRequestRepository.countByOperatorIdAndStatus(operatorId,
                                ProcurementStatus.SUBMITTED);
                long approvedPRs = procurementRequestRepository.countByOperatorIdAndStatus(operatorId,
                                ProcurementStatus.APPROVED);
                long rejectedPRs = procurementRequestRepository.countByOperatorIdAndStatus(operatorId,
                                ProcurementStatus.REJECTED);
                long escalatedPRs = procurementRequestRepository.countByOperatorIdAndStatus(operatorId,
                                ProcurementStatus.ESCALATED);
                long returnedPRs = procurementRequestRepository.countByOperatorIdAndStatus(operatorId,
                                ProcurementStatus.RETURNED);

                // Calculate financial metrics
                BigDecimal totalValue = BigDecimal.ZERO; // TODO: Sum from DB
                BigDecimal pendingValue = BigDecimal.ZERO; // TODO: Sum submitted + escalated

                // Time-based counts
                LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0)
                                .withSecond(0);
                LocalDateTime weekStart = LocalDateTime.now().minusDays(7);

                long prsThisMonth = procurementRequestRepository.countByOperatorIdAndCreatedAtAfter(operatorId,
                                monthStart);
                long prsThisWeek = procurementRequestRepository.countByOperatorIdAndCreatedAtAfter(operatorId,
                                weekStart);

                return OperatorDashboardDTO.builder()
                                .totalPRs((int) totalPRs)
                                .draftPRs((int) draftPRs)
                                .submittedPRs((int) submittedPRs)
                                .approvedPRs((int) approvedPRs)
                                .rejectedPRs((int) rejectedPRs)
                                .escalatedPRs((int) escalatedPRs)
                                .returnedPRs((int) returnedPRs)
                                .totalValue(totalValue)
                                .pendingValue(pendingValue)
                                .prsThisMonth((int) prsThisMonth)
                                .prsThisWeek((int) prsThisWeek)
                                .generatedAt(LocalDateTime.now())
                                .build();
        }

        /**
         * Get PR status breakdown
         */
        @Transactional(readOnly = true)
        public PRStatusBreakdownDTO getPRStatusBreakdown(UUID operatorId) {
                log.info("Getting PR status breakdown for operator: {}", operatorId);

                Map<String, Integer> statusCounts = new LinkedHashMap<>();
                Map<String, BigDecimal> statusValues = new LinkedHashMap<>();

                // Count each status
                for (ProcurementStatus status : ProcurementStatus.values()) {
                        long count = procurementRequestRepository.countByOperatorIdAndStatus(operatorId, status);
                        statusCounts.put(status.name(), (int) count);
                        statusValues.put(status.name(), BigDecimal.ZERO); // TODO: Sum values
                }

                return PRStatusBreakdownDTO.builder()
                                .statusCounts(statusCounts)
                                .statusValues(statusValues)
                                .build();
        }

        /**
         * Get monthly statistics
         */
        @Transactional(readOnly = true)
        public MonthlyPRStatsDTO getMonthlyStats(UUID operatorId, int year, int month) {
                log.info("Getting monthly stats for operator: {}, year: {}, month: {}", operatorId, year, month);

                LocalDateTime monthStart = LocalDateTime.of(year, month, 1, 0, 0);
                LocalDateTime monthEnd = monthStart.plusMonths(1);

                long totalPRs = procurementRequestRepository.countByOperatorIdAndCreatedAtBetween(
                                operatorId, monthStart, monthEnd);

                long approvedPRs = procurementRequestRepository.countByOperatorIdAndStatusAndUpdatedAtBetween(
                                operatorId, ProcurementStatus.APPROVED, monthStart, monthEnd);

                long rejectedPRs = procurementRequestRepository.countByOperatorIdAndStatusAndUpdatedAtBetween(
                                operatorId, ProcurementStatus.REJECTED, monthStart, monthEnd);

                return MonthlyPRStatsDTO.builder()
                                .year(year)
                                .month(month)
                                .totalPRs((int) totalPRs)
                                .approvedPRs((int) approvedPRs)
                                .rejectedPRs((int) rejectedPRs)
                                .totalValue(BigDecimal.ZERO) // TODO: Sum from DB
                                .approvedValue(BigDecimal.ZERO) // TODO: Sum from DB
                                .build();
        }

        /**
         * Get dashboard stats as Map (for flexible response structure)
         */
        @Transactional(readOnly = true)
        public Map<String, Object> getDashboardStats(UUID operatorId) {
                log.info("Getting dashboard stats for operator: {}", operatorId);

                OperatorDashboardDTO dashboard = getOperatorDashboard(operatorId);

                Map<String, Object> stats = new LinkedHashMap<>();
                stats.put("totalPRs", dashboard.getTotalPRs());
                stats.put("draftPRs", dashboard.getDraftPRs());
                stats.put("submittedPRs", dashboard.getSubmittedPRs());
                stats.put("approvedPRs", dashboard.getApprovedPRs());
                stats.put("rejectedPRs", dashboard.getRejectedPRs());
                stats.put("escalatedPRs", dashboard.getEscalatedPRs());
                stats.put("returnedPRs", dashboard.getReturnedPRs());
                stats.put("totalValue", dashboard.getTotalValue());
                stats.put("pendingValue", dashboard.getPendingValue());
                stats.put("prsThisMonth", dashboard.getPrsThisMonth());
                stats.put("prsThisWeek", dashboard.getPrsThisWeek());
                stats.put("generatedAt", dashboard.getGeneratedAt());

                return stats;
        }
}
