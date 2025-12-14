package com.tugas_akhir.admin_service.calendar.service;

import com.tugas_akhir.admin_service.calendar.dto.CalendarDTO;
import com.tugas_akhir.admin_service.calendar.dto.NonActiveDayDTO;
import com.tugas_akhir.admin_service.calendar.entity.Calendar;
import com.tugas_akhir.admin_service.calendar.entity.NonActiveDay;
import com.tugas_akhir.admin_service.calendar.mapper.CalendarMapper;
import com.tugas_akhir.admin_service.calendar.repository.CalendarRepository;
import com.tugas_akhir.admin_service.calendar.repository.NonActiveDayRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final NonActiveDayRepository nonActiveDayRepository;
    private final CalendarMapper calendarMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional(readOnly = true)
    public List<CalendarDTO> getAllCalendarEntries() {
        return calendarRepository.findAll().stream()
                .map(calendarMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CalendarDTO updateCalendarEntry(CalendarDTO dto) {
        Calendar calendar = calendarRepository.findById(dto.getCalendarDate())
                .orElse(new Calendar(dto.getCalendarDate(), true, false, null));

        calendar.setWorkingDay(dto.isWorkingDay());
        calendar.setCutoff(dto.isCutoff());
        calendar.setDescription(dto.getDescription());

        Calendar savedCalendar = calendarRepository.save(calendar);

        kafkaTemplate.send("admin.calendar.events", "calendar.updated", savedCalendar.getCalendarDate().toString());

        if (savedCalendar.isCutoff()) {
            kafkaTemplate.send("admin.calendar.cutoff.updated", "calendar.cutoff.updated",
                    savedCalendar.getCalendarDate().toString());
        }

        return calendarMapper.toDTO(savedCalendar);
    }

    @Transactional
    public CalendarDTO setMonthlyCutoff(LocalDate date) {
        Calendar calendar = calendarRepository.findById(date)
                .orElse(new Calendar(date, true, true, "Monthly Cutoff"));

        calendar.setCutoff(true);
        Calendar savedCalendar = calendarRepository.save(calendar);

        kafkaTemplate.send("admin.calendar.cutoff.updated", "calendar.cutoff.updated",
                savedCalendar.getCalendarDate().toString());

        return calendarMapper.toDTO(savedCalendar);
    }

    @Transactional
    public NonActiveDayDTO addNonActiveDay(NonActiveDayDTO dto) {
        if (!nonActiveDayRepository.findOverlappingPeriods(dto.getStartDate(), dto.getEndDate()).isEmpty()) {
            throw new IllegalArgumentException("Overlapping non-active period exists.");
        }

        NonActiveDay nonActiveDay = calendarMapper.toEntity(dto);
        NonActiveDay saved = nonActiveDayRepository.save(nonActiveDay);

        // Also update individual calendar entries for this range
        LocalDate current = dto.getStartDate();
        while (!current.isAfter(dto.getEndDate())) {
            Calendar calendar = calendarRepository.findById(current)
                    .orElse(new Calendar(current, true, false, null));
            calendar.setWorkingDay(false);
            calendar.setDescription(dto.getReason());
            calendarRepository.save(calendar);
            current = current.plusDays(1);
        }

        kafkaTemplate.send("admin.calendar.events", "nonactive.day.added", saved.getNonActiveId().toString());

        return calendarMapper.toDTO(saved);
    }
}
