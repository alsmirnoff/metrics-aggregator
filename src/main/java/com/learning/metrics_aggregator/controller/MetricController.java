package com.learning.metrics_aggregator.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.learning.metrics_aggregator.dto.MetricRequest;
import com.learning.metrics_aggregator.entity.Application;
import com.learning.metrics_aggregator.service.ApplicationService;
import com.learning.metrics_aggregator.service.MetricService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class MetricController {

    private final ApplicationService applicationService;
    private final MetricService metricService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Application createApplication(@RequestBody Application application) {
        return applicationService.create(application);
    }

    @PostMapping("/{appId}/metrics")
    public ResponseEntity<String> getMetrics(@PathVariable UUID appId, @Valid @RequestBody MetricRequest metricRequest) {
        try {
            metricService.processMetrics(appId, metricRequest);
            return ResponseEntity.accepted().body("Metrics accepted for processing");
            
        } catch (IllegalArgumentException e) {
            // log.warn("Application not found: {}", appId);
            return ResponseEntity.badRequest().body("Application not found: " + appId);
            
        } catch (Exception e) {
            // log.error("Error processing metrics for application: {}", appId, e);
            return ResponseEntity.internalServerError().body("Error processing metrics");
        }
    }
    
}
