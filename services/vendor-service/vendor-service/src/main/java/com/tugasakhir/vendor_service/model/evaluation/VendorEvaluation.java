package com.tugasakhir.vendor_service.model.evaluation;

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
@Table(name = "vendor_evaluations")
public class VendorEvaluation extends BaseEntity {

    @Id
    private String evalId;

    private String vendorId;
    private String periode;
    private Double onTimeRate;
    private Double returRate;
    private Double leadTimeAvg;
    private Double skor;
}
