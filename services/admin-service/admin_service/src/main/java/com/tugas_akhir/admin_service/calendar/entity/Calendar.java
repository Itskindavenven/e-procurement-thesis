package com.tugas_akhir.admin_service.calendar.entity;

import com.tugas_akhir.admin_service.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "calendars")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Calendar extends BaseEntity {

    @Id
    @Column(name = "calendar_date")
    private LocalDate calendarDate;

    @Column(name = "is_working_day", nullable = false)
    private boolean isWorkingDay = true;

    @Column(name = "is_cutoff", nullable = false)
    private boolean isCutoff = false;

    @Column(length = 255)
    private String description;
}
