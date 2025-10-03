package com.learning.metrics_aggregator.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class MetricMessage {
    
    private UUID id;
    private UUID applicationId;
    private Double cpuUsage;
    private Double memUsage;
    private Long requestCount;
    private Double responseTime;
    private Integer activeConnections;
    private Integer errorCount;
    private LocalDateTime timestamp;
    private LocalDateTime receivedAt;
    
    public MetricMessage() {
        this.id = UUID.randomUUID();
        this.receivedAt = LocalDateTime.now();
    }
}
