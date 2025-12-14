package com.tugasakhir.vendor_service.model.paymentterm;

import com.tugasakhir.vendor_service.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_terms")
public class PaymentTerm extends BaseEntity {

    @Id
    private String termId;

    private String contractId;
    private String poId;
    private String deskripsi;
    private Double persentase;
    private BigDecimal nominal;
    private LocalDate dueDate;
    private String statusTerm; // PENDING, PAID
}
