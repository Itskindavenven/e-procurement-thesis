package com.tugasakhir.vendor_service.model.progressreport;

import com.tugasakhir.vendor_service.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "progress_reports")
public class ProgressReport extends BaseEntity {

    @Id
    private String progressId;

    private String contractId;
    private String periode;
    private Double persenProgress;
    private String lampiranUrl;
    private String catatan;
}
