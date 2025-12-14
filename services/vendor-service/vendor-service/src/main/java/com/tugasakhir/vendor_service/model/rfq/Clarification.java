package com.tugasakhir.vendor_service.model.rfq;

import com.tugasakhir.vendor_service.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clarifications")
public class Clarification extends BaseEntity {

    @Id
    private String clarificationId;

    private String rfqId;
    private String vendorId;
    private String question;
    private String answer;
    private String status; // OPEN, ANSWERED
    private LocalDateTime timestamp;
}
