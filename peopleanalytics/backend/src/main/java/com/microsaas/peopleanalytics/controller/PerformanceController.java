package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.model.PerformanceMetric;
import com.microsaas.peopleanalytics.service.PerformanceAggregationService;
import com.microsaas.peopleanalytics.dto.PerformanceMetricDto;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/performance-metrics")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceAggregationService service;

    @GetMapping
    public ResponseEntity<List<PerformanceMetric>> getMetrics(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }

    @PostMapping
    public ResponseEntity<PerformanceMetric> addMetric(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody PerformanceMetricDto dto) {
        return ResponseEntity.ok(service.addMetric(tenantId, dto));
    }
}
