package com.microsaas.deploysignal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/metrics/dora")
public class DoraMetricsController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoraMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("deploymentFrequency", "Daily");
        metrics.put("leadTimeForChanges", "4 hours");
        metrics.put("timeToRestoreService", "1 hour");
        metrics.put("changeFailureRate", "5%");
        
        return ResponseEntity.ok(metrics);
    }
}
