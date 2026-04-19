package com.microsaas.debtnavigator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/debt")
public class HealthMetricsController {
    @GetMapping("/health/contracts")
    public ResponseEntity<String> healthContracts() {
        return ResponseEntity.ok("Contracts are healthy");
    }

    @GetMapping("/metrics/summary")
    public ResponseEntity<String> metricsSummary() {
        return ResponseEntity.ok("Metrics summary");
    }
}
