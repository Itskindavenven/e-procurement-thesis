package com.tugasakhir.vendor_service.dto.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminEventDto {
    private String eventType;
    private String vendorId;
    private Long timestamp;
}
