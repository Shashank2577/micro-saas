package com.microsaas.telemetrycore.controller;

import com.microsaas.telemetrycore.model.Metric;
import com.microsaas.telemetrycore.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    private final MetricService metricService;

    @Autowired
    public MetricsController(MetricService metricService) {
        this.metricService = metricService;
    }

    @PostMapping
    public ResponseEntity<Metric> createMetric(@RequestBody Metric metric) {
        return ResponseEntity.ok(metricService.createMetric(metric));
    }

    @GetMapping
    public ResponseEntity<List<Metric>> getAllMetrics() {
        return ResponseEntity.ok(metricService.getAllMetrics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Metric> getMetric(@PathVariable UUID id) {
        return ResponseEntity.ok(metricService.getMetric(id));
    }

    @GetMapping("/{id}/data")
    public ResponseEntity<List<Map<String, Object>>> getMetricData(
            @PathVariable UUID id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDate,
            @RequestParam(defaultValue = "1h") String interval) {
        return ResponseEntity.ok(metricService.getMetricData(id, startDate, endDate, interval));
    }
}
