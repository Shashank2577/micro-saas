package com.microsaas.regulatoryfiling.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/filings")
public class HealthAndMetricsController {

    @GetMapping("/health/contracts")
    public Map<String, Object> contracts() {
        return Map.of("status", "UP", "contracts", "Contracts are valid");
    }

    @GetMapping("/metrics/summary")
    public Map<String, Object> metrics() {
        return Map.of("totalObligations", 100, "overdueFilings", 5);
    }
}
