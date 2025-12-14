package com.tugas_akhir.admin_service.calendar.controller;

import com.tugas_akhir.admin_service.calendar.dto.CalendarDTO;
import com.tugas_akhir.admin_service.calendar.dto.NonActiveDayDTO;
import com.tugas_akhir.admin_service.calendar.service.CalendarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CalendarDTO>> getAllCalendarEntries() {
        return ResponseEntity.ok(calendarService.getAllCalendarEntries());
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CalendarDTO> updateCalendarEntry(@Valid @RequestBody CalendarDTO dto) {
        return ResponseEntity.ok(calendarService.updateCalendarEntry(dto));
    }

    @PostMapping("/cutoff")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CalendarDTO> setMonthlyCutoff(@RequestBody java.util.Map<String, String> payload) {
        java.time.LocalDate date = java.time.LocalDate.parse(payload.get("date"));
        return ResponseEntity.ok(calendarService.setMonthlyCutoff(date));
    }

    @PostMapping("/non-active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NonActiveDayDTO> addNonActiveDay(@Valid @RequestBody NonActiveDayDTO dto) {
        return ResponseEntity.ok(calendarService.addNonActiveDay(dto));
    }
}
