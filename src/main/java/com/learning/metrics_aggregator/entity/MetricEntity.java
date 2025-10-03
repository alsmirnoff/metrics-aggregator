package com.learning.metrics_aggregator.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "metrics")
@Data
public class MetricEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID applicationId;

    @Column(nullable = false)
    private Double cpuUsage;

    @Column(nullable = false)
    private Double memUsage;

    @Column(nullable = false)
    private Long requestCount;

    private Double responseTime;
    private Integer activeConnections;
    private Integer errorCount;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private LocalDateTime receivedAt;

    @Column(nullable = false)
    private LocalDateTime processedAt;
}
