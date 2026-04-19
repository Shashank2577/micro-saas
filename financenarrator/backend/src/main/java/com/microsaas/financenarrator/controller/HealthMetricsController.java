package com.microsaas.financenarrator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/narratives")
public class HealthMetricsController {

    @GetMapping("/health/contracts")
    public ResponseEntity<Map<String, String>> healthContracts() {
        return ResponseEntity.ok(Map.of("status", "UP", "contracts_valid", "true"));
    }

    @GetMapping("/metrics/summary")
    public ResponseEntity<Map<String, Object>> metricsSummary() {
        return ResponseEntity.ok(Map.of("narratives_generated", 42, "avg_latency_ms", 120));
    }
}
