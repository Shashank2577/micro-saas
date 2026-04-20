package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.dto.ValidateResponse;
import com.microsaas.peopleanalytics.model.HeadcountMetric;
import com.microsaas.peopleanalytics.service.HeadcountMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/headcount-metrics")
@RequiredArgsConstructor
public class HeadcountController {

    private final HeadcountMetricService service;

    @GetMapping
    public ResponseEntity<List<HeadcountMetric>> findAll(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.findAll(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeadcountMetric> findById(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.findById(id, tenantId));
    }

    @PostMapping
    public ResponseEntity<HeadcountMetric> create(@RequestBody HeadcountMetric entity, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.create(entity, tenantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HeadcountMetric> update(@PathVariable UUID id, @RequestBody HeadcountMetric entity, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.update(id, entity, tenantId));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<ValidateResponse> validate(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.validate(id, tenantId));
    }
}
