package com.tugas_akhir.procurement_service.domain.dashboard.service;

import com.tugas_akhir.procurement_service.common.enums.ProcurementStatus;
import com.tugas_akhir.procurement_service.domain.audit.repository.ApprovalHistoryRepository;
import com.tugas_akhir.procurement_service.domain.dashboard.dto.DashboardDTOs.*;
import com.tugas_akhir.procurement_service.domain.procurementrequest.repository.ProcurementRequestRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Service for supervisor dashboard data
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardSupervisorService {

    private final ProcurementRequestRepository procurementRequestRepository;
    private final ApprovalHistoryRepository approvalHistoryRepository;

    /**
     * Get supervisor dashboard summary
     */
    @Transactional(readOnly = true)
    public SupervisorDashboardDTO getSupervisorDashboard() {
        log.info("Generating supervisor dashboard");

        // Pending approvals
        long pendingApprovals = procurementRequestRepository.countByStatus(ProcurementStatus.SUBMITTED);
        long escalatedApprovals = procurementRequestRepository.countByStatus(ProcurementStatus.ESCALATED);

        // TODO: Count urgent (priority = URGENT and status = SUBMITTED/ESCALATED)
        long urgentApprovals = 0;

        // Time-based approval counts
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime weekStart = LocalDateTime.now().minusDays(7);
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);

        long approvedToday = procurementRequestRepository.countByStatusAndUpdatedAtAfter(
                ProcurementStatus.APPROVED, today);
        long approvedThisWeek = procurementRequestRepository.countByStatusAndUpdatedAtAfter(
                ProcurementStatus.APPROVED, weekStart);
        long approvedThisMonth = procurementRequestRepository.countByStatusAndUpdatedAtAfter(
                ProcurementStatus.APPROVED, monthStart);
        long rejectedThisMonth = procurementRequestRepository.countByStatusAndUpdatedAtAfter(
                ProcurementStatus.REJECTED, monthStart);

        // Performance metrics
        // TODO: Calculate average approval time from approval_histories
        Double averageApprovalTimeHours = 24.5; // Placeholder

        // TODO: Find oldest pending PR
        Integer oldestPendingDays = 0;

        // Financial metrics
        BigDecimal pendingApprovalValue = BigDecimal.ZERO; // TODO: Sum from DB
        BigDecimal approvedValueThisMonth = BigDecimal.ZERO; // TODO: Sum from DB

        return SupervisorDashboardDTO.builder()
                .pendingApprovals((int) pendingApprovals)
                .escalatedApprovals((int) escalatedApprovals)
                .urgentApprovals((int) urgentApprovals)
                .approvedToday((int) approvedToday)
                .approvedThisWeek((int) approvedThisWeek)
                .approvedThisMonth((int) approvedThisMonth)
                .rejectedThisMonth((int) rejectedThisMonth)
                .averageApprovalTimeHours(averageApprovalTimeHours)
                .oldestPendingDays(oldestPendingDays)
                .pendingApprovalValue(pendingApprovalValue)
                .approvedValueThisMonth(approvedValueThisMonth)
                .generatedAt(LocalDateTime.now())
                .build();
    }
}
