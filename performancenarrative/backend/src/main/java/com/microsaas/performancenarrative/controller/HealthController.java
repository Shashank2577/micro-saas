package com.microsaas.performancenarrative.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/performance")
public class HealthController {

    @GetMapping("/health/contracts")
    public Map<String, String> checkContracts() {
        return Map.of("status", "OK", "contracts", "3/3 active");
    }

    @GetMapping("/metrics/summary")
    public Map<String, String> metricsSummary() {
        return Map.of("reviews", "100", "calibrations", "5");
    }
}
