package com.learning.metrics_aggregator.service;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.learning.metrics_aggregator.dao.ApplicationRepository;
import com.learning.metrics_aggregator.dto.MetricRequest;
import com.learning.metrics_aggregator.model.MetricMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MetricProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationRepository applicationRepository;

    @Value("${rabbitmq.exchange.name:metrics-exchange}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key:metrics}")
    private String routingKey;

    public void processMetrics(UUID appId, MetricRequest metricRequest){
        if(!applicationRepository.existsById(appId)) {
            throw new IllegalArgumentException("Application not found: " + appId);
        }

        MetricMessage message = new MetricMessage();
        message.setApplicationId(appId);
        message.setCpuUsage(metricRequest.getCpuUsage());
        message.setMemUsage(metricRequest.getMemUsage());
        message.setRequestCount(metricRequest.getRequestCount());
        message.setResponseTime(metricRequest.getResponseTime());
        message.setActiveConnections(metricRequest.getActiveConnections());
        message.setErrorCount(metricRequest.getErrorCount());
        message.setTimestamp(metricRequest.getTimestamp());

        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }

}
