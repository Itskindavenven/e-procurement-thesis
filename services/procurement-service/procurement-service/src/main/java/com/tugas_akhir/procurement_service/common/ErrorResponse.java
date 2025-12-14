package com.tugas_akhir.procurement_service.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Standardized error response format
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * Timestamp when the error occurred
     */
    private LocalDateTime timestamp;

    /**
     * HTTP status code
     */
    private Integer status;

    /**
     * Application-specific error code
     */
    private String errorCode;

    /**
     * Human-readable error message
     */
    private String message;

    /**
     * Request path that caused the error
     */
    private String path;
}
