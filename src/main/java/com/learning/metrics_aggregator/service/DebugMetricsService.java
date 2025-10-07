package com.learning.metrics_aggregator.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class DebugMetricsService {
    
    private final MeterRegistry meterRegistry;
    private final AtomicInteger testCounter = new AtomicInteger(0);
    private final Random random = new Random();
    
    @PostConstruct
    public void initDebugMetrics() {
        log.info("Registering debug metrics...");
        
        // 1. Простая счетчик метрика
        Gauge.builder("debug.test.counter", testCounter, AtomicInteger::get)
                .description("Simple test counter")
                .register(meterRegistry);
        
        // 2. Метрика с тегами (как у вас должно быть)
        Gauge.builder("debug.application.cpu.usage", this, 
                      service -> random.nextDouble() * 100)
                .tags("application", "test-app-1")
                .description("Test CPU usage")
                .register(meterRegistry);
        
        // 3. Еще одна метрика с другим тегом
        Gauge.builder("debug.application.cpu.usage", this,
                      service -> random.nextDouble() * 100)
                .tags("application", "test-app-2")
                .description("Test CPU usage")
                .register(meterRegistry);
        
        log.info("Debug metrics registered successfully");
    }
    
    // Метод для обновления счетчика (можно вызвать из контроллера для теста)
    public void incrementCounter() {
        testCounter.incrementAndGet();
    }
}