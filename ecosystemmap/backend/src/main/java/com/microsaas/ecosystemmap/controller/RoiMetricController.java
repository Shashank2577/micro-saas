package com.microsaas.ecosystemmap.controller;

import com.microsaas.ecosystemmap.entity.RoiMetric;
import com.microsaas.ecosystemmap.service.RoiTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ecosystems/{ecosystemId}/roi")
@RequiredArgsConstructor
public class RoiMetricController {

    private final RoiTrackingService roiTrackingService;

    @GetMapping
    public ResponseEntity<List<RoiMetric>> getMetricsByEcosystem(@PathVariable UUID ecosystemId) {
        return ResponseEntity.ok(roiTrackingService.getMetricsByEcosystem(ecosystemId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoiMetric> getMetricById(@PathVariable UUID ecosystemId, @PathVariable UUID id) {
        return ResponseEntity.ok(roiTrackingService.getMetricById(id));
    }

    @PostMapping
    public ResponseEntity<RoiMetric> createMetric(
            @PathVariable UUID ecosystemId,
            @RequestParam(required = false) UUID nodeId,
            @RequestBody RoiMetric metric) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roiTrackingService.createMetric(ecosystemId, nodeId, metric));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoiMetric> updateMetric(
            @PathVariable UUID ecosystemId,
            @PathVariable UUID id,
            @RequestBody RoiMetric metric) {
        return ResponseEntity.ok(roiTrackingService.updateMetric(id, metric));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetric(@PathVariable UUID ecosystemId, @PathVariable UUID id) {
        roiTrackingService.deleteMetric(id);
        return ResponseEntity.noContent().build();
    }
}
