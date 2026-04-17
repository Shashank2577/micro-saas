package com.microsaas.observabilitystack.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class MockObservabilityController {

    @PostMapping("/logs/ingest")
    public String ingestLog(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody Map<String, Object> log) {
        return "Log ingested";
    }

    @GetMapping("/logs/search")
    public List<Map<String, Object>> searchLogs(@RequestHeader("X-Tenant-ID") String tenantId, @RequestParam String query) {
        return List.of(Map.of("message", "Test log containing " + query));
    }

    @GetMapping("/logs/{id}")
    public Map<String, Object> getLog(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable String id) {
        return Map.of("id", id, "message", "Test log details");
    }

    @PostMapping("/logs/export")
    public String exportLogs(@RequestHeader("X-Tenant-ID") String tenantId) {
        return "Export started";
    }

    @PostMapping("/metrics/push")
    public String pushMetrics(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody Map<String, Object> metric) {
        return "Metrics pushed";
    }

    @GetMapping("/metrics/query")
    public Map<String, Object> queryMetrics(@RequestHeader("X-Tenant-ID") String tenantId, @RequestParam String query) {
        return Map.of("value", 42);
    }

    @GetMapping("/metrics/range")
    public List<Map<String, Object>> rangeMetrics(@RequestHeader("X-Tenant-ID") String tenantId) {
        return List.of(Map.of("timestamp", System.currentTimeMillis(), "value", 42));
    }

    @GetMapping("/metrics/labels")
    public List<String> getLabels(@RequestHeader("X-Tenant-ID") String tenantId) {
        return List.of("cpu_usage", "memory_usage");
    }

    @PostMapping("/traces/ingest")
    public String ingestTrace(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody Map<String, Object> trace) {
        return "Trace ingested";
    }

    @GetMapping("/traces/{id}")
    public Map<String, Object> getTrace(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable String id) {
        return Map.of("id", id, "spans", List.of());
    }

    @GetMapping("/traces/search")
    public List<Map<String, Object>> searchTraces(@RequestHeader("X-Tenant-ID") String tenantId) {
        return List.of(Map.of("id", UUID.randomUUID().toString()));
    }

    @GetMapping("/traces/{id}/waterfall")
    public Map<String, Object> getTraceWaterfall(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable String id) {
        return Map.of("waterfall", "data");
    }
}
