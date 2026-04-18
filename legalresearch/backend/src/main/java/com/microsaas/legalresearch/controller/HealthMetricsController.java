package com.microsaas.legalresearch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/research")
public class HealthMetricsController {

    @GetMapping("/health/contracts")
    public ResponseEntity<Map<String, String>> healthContracts() {
        return ResponseEntity.ok(Map.of("status", "up"));
    }

    @GetMapping("/metrics/summary")
    public ResponseEntity<Map<String, Object>> metricsSummary() {
        return ResponseEntity.ok(Map.of("queries_processed", 42, "memos_generated", 10));
    }
}
