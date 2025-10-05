package com.learning.metrics_aggregator.consumer;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.learning.metrics_aggregator.dao.MetricRepository;
import com.learning.metrics_aggregator.entity.MetricEntity;
import com.learning.metrics_aggregator.model.MetricMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MetricConsumer {

    private final MetricRepository metricRepository;

    @RabbitListener(queues = "${rabbitmq.queue.name:metrics-queue}")
    @Transactional
    public void processMetric(MetricMessage metricMessage) {
        try {
            MetricEntity metricEntity = new MetricEntity();
            metricEntity.setApplicationId(metricMessage.getApplicationId());
            metricEntity.setCpuUsage(metricMessage.getCpuUsage());
            metricEntity.setMemUsage(metricMessage.getMemUsage());
            metricEntity.setRequestCount(metricMessage.getRequestCount());
            metricEntity.setResponseTime(metricMessage.getResponseTime());
            metricEntity.setActiveConnections(metricMessage.getActiveConnections());
            metricEntity.setErrorCount(metricMessage.getErrorCount());
            metricEntity.setTimestamp(metricMessage.getTimestamp());
            metricEntity.setReceivedAt(metricMessage.getReceivedAt());
            metricEntity.setProcessedAt(LocalDateTime.now());

            metricRepository.save(metricEntity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process metric", e);
        }
    }

}
