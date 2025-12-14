package com.tugas_akhir.admin_service.calendar.repository;

import com.tugas_akhir.admin_service.calendar.entity.NonActiveDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface NonActiveDayRepository extends JpaRepository<NonActiveDay, UUID> {

    @Query("SELECT n FROM NonActiveDay n WHERE n.startDate <= :endDate AND n.endDate >= :startDate")
    List<NonActiveDay> findOverlappingPeriods(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
