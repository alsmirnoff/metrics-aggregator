package com.learning.metrics_aggregator.dao;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.metrics_aggregator.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    public boolean existsByName(String name);
}
