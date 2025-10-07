package com.learning.metrics_aggregator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.metrics_aggregator.service.DebugMetricsService;

@RestController
@RequiredArgsConstructor
public class DebugController {
    
    private final DebugMetricsService debugMetricsService;
    
    @PostMapping("/debug/increment")
    public String incrementDebugMetric() {
        debugMetricsService.incrementCounter();
        return "Counter incremented";
    }
}