package com.tugas_akhir.admin_service.calendar.repository;

import com.tugas_akhir.admin_service.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, LocalDate> {
}
