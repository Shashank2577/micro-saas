package com.microsaas.financenarrator.controller;

import com.microsaas.financenarrator.model.SupportingMetric;
import com.microsaas.financenarrator.service.SupportingMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/narratives/supporting-metrics")
@RequiredArgsConstructor
public class SupportingMetricController {
    private final SupportingMetricService service;

    @GetMapping
    public ResponseEntity<List<SupportingMetric>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping
    public ResponseEntity<SupportingMetric> create(@RequestBody SupportingMetric dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportingMetric> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SupportingMetric> update(@PathVariable UUID id, @RequestBody SupportingMetric dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.validate(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/simulate")
    public ResponseEntity<Void> simulate(@PathVariable UUID id) {
        service.simulate(id);
        return ResponseEntity.ok().build();
    }
}
