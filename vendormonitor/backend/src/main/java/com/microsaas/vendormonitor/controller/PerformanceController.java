package com.microsaas.vendormonitor.controller;

import com.microsaas.vendormonitor.domain.PerformanceRecord;
import com.microsaas.vendormonitor.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/vendors/{vendorId}")
public class PerformanceController {

    private final PerformanceService performanceService;

    @Autowired
    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @GetMapping("/performance")
    public ResponseEntity<List<PerformanceRecord>> getPerformanceRecords(@PathVariable UUID vendorId) {
        return ResponseEntity.ok(performanceService.getPerformanceRecordsForVendor(vendorId));
    }

    @PostMapping("/performance")
    public ResponseEntity<PerformanceRecord> recordPerformance(@PathVariable UUID vendorId, @RequestBody PerformanceRecord record) {
        try {
            return ResponseEntity.ok(performanceService.recordPerformance(vendorId, record));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/metrics-summary")
    public ResponseEntity<Map<String, Object>> getMetricsSummary(@PathVariable UUID vendorId) {
        return ResponseEntity.ok(performanceService.getMetricsSummary(vendorId));
    }
}
