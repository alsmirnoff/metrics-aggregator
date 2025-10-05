package com.learning.metrics_aggregator.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

@Component
public class MetricService {

    private final MeterRegistry meterRegistry;
    private final Map<String, Double> currentCpuUsageMap = new ConcurrentHashMap<>();
    private final Map<String, Double> currentMemUsageMap = new ConcurrentHashMap<>();
    private final Map<String, Long> currentTotalRequestsMap = new ConcurrentHashMap<>();
    private final Map<String, Double> currentResponseTimeMap = new ConcurrentHashMap<>();

    public MetricService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void updateMetrics(String appId, double cpuUsage, double memUsage, long totalRequests, double responseTime) {
        currentCpuUsageMap.put(appId, cpuUsage);
        currentMemUsageMap.put(appId, memUsage);
        currentTotalRequestsMap.put(appId, totalRequests);
        currentResponseTimeMap.put(appId, responseTime);

        Gauge.builder("application.cpu.usage", currentCpuUsageMap, map -> map.getOrDefault(appId, 0.0))
            .tag("application_id", appId)
            .description("CPU usage of a specific application")
            .register(meterRegistry);

        Gauge.builder("application.memory.usage", currentMemUsageMap, map -> map.getOrDefault(appId, 0.0))
            .tag("application_id", appId)
            .description("Memory usage of a specific application")
            .register(meterRegistry);

        Gauge.builder("application.requests.total", currentTotalRequestsMap, map -> map.getOrDefault(appId, 0l))
            .tag("application_id", appId)
            .description("Total requests of a specific application")
            .register(meterRegistry);

        Gauge.builder("application.response.time", currentResponseTimeMap, map -> map.getOrDefault(appId, 0.0))
            .tag("application_id", appId)
            .description("Response time of a specific application")
            .register(meterRegistry);

    }

}
