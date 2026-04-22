package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.model.PerformanceMetric;
import com.microsaas.peopleanalytics.repository.PerformanceMetricRepository;
import com.microsaas.peopleanalytics.service.PerformanceAggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/people-analytics/performance")
@RequiredArgsConstructor
public class PerformanceController {
    private final PerformanceAggregationService performanceAggregationService;
    private final PerformanceMetricRepository performanceMetricRepository;

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboard() {
        return performanceAggregationService.getPerformanceDashboardData();
    }

    @PostMapping("/metrics")
    public ResponseEntity<Void> ingestMetrics(@RequestBody PerformanceMetric metric) {
        // Implementation for metric ingestion placeholder
        return ResponseEntity.ok().build();
    }
}
