package com.microsaas.performancenarrative;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.microsaas.performancenarrative.repository")
@EntityScan(basePackages = "com.microsaas.performancenarrative.entity")
public class PerformanceNarrativeApplication {
    public static void main(String[] args) {
        SpringApplication.run(PerformanceNarrativeApplication.class, args);
    }
}
