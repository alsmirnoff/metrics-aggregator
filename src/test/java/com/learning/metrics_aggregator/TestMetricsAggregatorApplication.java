package com.learning.metrics_aggregator;

import org.springframework.boot.SpringApplication;

public class TestMetricsAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.from(MetricsAggregatorApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
