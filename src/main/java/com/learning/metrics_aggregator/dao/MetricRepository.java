package com.learning.metrics_aggregator.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.learning.metrics_aggregator.entity.MetricEntity;

public interface MetricRepository extends JpaRepository<MetricEntity, UUID> {

    List<MetricEntity> findByApplicationIdAndTimestampBetween(UUID applicationId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT m FROM MetricEntity m WHERE m.applicationId = :appId AND m.timestamp >= :since")
    List<MetricEntity> findRecentMetrics(@Param("appId") UUID appId, @Param("since") LocalDateTime since);

}
