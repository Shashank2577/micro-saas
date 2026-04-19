package com.microsaas.contractportfolio.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/contracts")
public class HealthMetricsController {

    @GetMapping("/health/contracts")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

    @GetMapping("/metrics/summary")
    public Map<String, Object> metrics() {
        return Map.of(
            "totalContracts", 100,
            "activeAlerts", 5,
            "averageRiskScore", 42.5
        );
    }
}
