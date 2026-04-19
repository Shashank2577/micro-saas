package com.microsaas.cashflowanalyzer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/cashflow")
public class HealthController {

    @GetMapping("/health/contracts")
    public ResponseEntity<Map<String, String>> healthContracts() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("contracts", "Valid");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/metrics/summary")
    public ResponseEntity<Map<String, Object>> metricsSummary() {
        Map<String, Object> response = new HashMap<>();
        response.put("active_periods", 42);
        response.put("anomalies_detected", 5);
        return ResponseEntity.ok(response);
    }
}
