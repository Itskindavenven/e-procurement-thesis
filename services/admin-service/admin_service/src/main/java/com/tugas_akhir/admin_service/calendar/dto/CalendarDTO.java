package com.tugas_akhir.admin_service.calendar.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CalendarDTO {
    @NotNull(message = "Date is required")
    private LocalDate calendarDate;

    private boolean isWorkingDay;
    private boolean isCutoff;
    private String description;
}
