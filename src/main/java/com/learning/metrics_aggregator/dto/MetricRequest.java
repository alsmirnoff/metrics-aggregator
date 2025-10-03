package com.learning.metrics_aggregator.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MetricRequest {

    @NotNull(message = "CPU usage cannot be null")
    @DecimalMin(value = "0.0", message = "CPU usage cannot be negative")
    @DecimalMax(value = "100.0", message = "CPU usage cannot exceed 100%")
    private Double cpuUsage;

    @NotNull(message = "Memory usage cannot be null")
    @DecimalMin(value = "0.0", message = "Memory usage cannot be negative")
    @DecimalMax(value = "100.0", message = "Memory usage cannot exceed 100%")
    private Double memUsage;

    @NotNull(message = "Request count cannot be null")
    @Min(value = 0, message = "Request count cannot be negative")
    private Long requestCount;

    private Double responseTime;

    private Integer activeConnections;

    private Integer errorCount;

    @NotNull(message = "Timestamp cannot be null")
    private LocalDateTime timestamp;
}
