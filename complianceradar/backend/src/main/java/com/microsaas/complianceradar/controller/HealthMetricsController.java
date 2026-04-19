package com.microsaas.complianceradar.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/regulations")
public class HealthMetricsController {

    @GetMapping("/health/contracts")
    public ResponseEntity<Map<String, String>> checkContracts() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }

    @GetMapping("/metrics/summary")
    public ResponseEntity<Map<String, String>> metricsSummary() {
        return ResponseEntity.ok(Map.of("status", "ACTIVE"));
    }
}
