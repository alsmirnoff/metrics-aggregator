package com.learning.metrics_aggregator.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.learning.metrics_aggregator.dao.MetricRepository;
import com.learning.metrics_aggregator.entity.MetricEntity;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MetricAggregationService {

    private final MetricRepository metricRepository;
    private final MeterRegistry meterRegistry;
    private final ConcurrentMap<UUID, AggregatedMetrics> currentMetrics = new ConcurrentHashMap<>();

    @Scheduled(fixedRate = 60000)
    public void aggregateMetrics() {
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        List<UUID> activeApplications = metricRepository.findDistinctApplicationIdsSince(fiveMinutesAgo);

        for (UUID appId : activeApplications) {
            aggregateMetricsForApplication(appId, fiveMinutesAgo);
        }
    }

    private void aggregateMetricsForApplication(UUID appId, LocalDateTime since){
        List<MetricEntity> metrics = metricRepository.findByApplicationIdAndTimestampGreaterThanEqual(appId, since);
        
        if (metrics.isEmpty()) {
            return;
        }

        double avgCpu = metrics.stream()
                .mapToDouble(MetricEntity::getCpuUsage)
                .average()
                .orElse(0.0);
                
        double avgMemory = metrics.stream()
                .mapToDouble(MetricEntity::getMemUsage)
                .average()
                .orElse(0.0);
                
        long totalRequests = metrics.stream()
                .mapToLong(MetricEntity::getRequestCount)
                .sum();
                
        double avgResponseTime = metrics.stream()
                .map(MetricEntity::getResponseTime)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        AggregatedMetrics aggregated = new AggregatedMetrics(avgCpu, avgMemory, totalRequests, avgResponseTime);
        currentMetrics.put(appId, aggregated);
        registerMetricsInMicrometer(appId);
    }

    private void registerMetricsInMicrometer(UUID appId) {
        String appIdString = appId.toString();
        Tags tags = Tags.of("application", appIdString);
        
        Gauge.builder("application.cpu.usage", currentMetrics, 
                     map -> getCurrentMetricValue(map, appId, metrics -> metrics.avgCpu))
            .tags(tags)
            .description("Average CPU usage for application")
            .register(meterRegistry);
            
        Gauge.builder("application.memory.usage", currentMetrics,
                     map -> getCurrentMetricValue(map, appId, metrics -> metrics.avgMemory))
            .tags(tags)
            .description("Average memory usage for application")
            .register(meterRegistry);
            
        Gauge.builder("application.requests.total", currentMetrics,
                     map -> getCurrentMetricValue(map, appId, metrics -> (double) metrics.totalRequests))
            .tags(tags)
            .description("Total requests for application")
            .register(meterRegistry);
            
        Gauge.builder("application.response.time.avg", currentMetrics,
                     map -> getCurrentMetricValue(map, appId, metrics -> metrics.avgResponseTime))
            .tags(tags)
            .description("Average response time for application")
            .register(meterRegistry);
    }
    
    private double getCurrentMetricValue(ConcurrentMap<UUID, AggregatedMetrics> map, UUID appId, 
                                       java.util.function.Function<AggregatedMetrics, Double> extractor) {
        AggregatedMetrics metrics = map.get(appId);
        return metrics != null ? extractor.apply(metrics) : 0.0;
    }

    private static class AggregatedMetrics {
        final double avgCpu;
        final double avgMemory;
        final long totalRequests;
        final double avgResponseTime;
        
        AggregatedMetrics(double avgCpu, double avgMemory, long totalRequests, double avgResponseTime) {
            this.avgCpu = avgCpu;
            this.avgMemory = avgMemory;
            this.totalRequests = totalRequests;
            this.avgResponseTime = avgResponseTime;
        }
    }
}
