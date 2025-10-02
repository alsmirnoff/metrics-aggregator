package com.learning.metrics_aggregator.service;

import org.springframework.stereotype.Service;

import com.learning.metrics_aggregator.entity.Application;

@Service
public class ApplicationService {
    public String create(Application application){
        return "application created";
    }
}
