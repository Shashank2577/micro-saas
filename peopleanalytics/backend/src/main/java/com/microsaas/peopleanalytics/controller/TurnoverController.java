package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.dto.ValidateResponse;
import com.microsaas.peopleanalytics.model.AttritionSignal;
import com.microsaas.peopleanalytics.service.AttritionSignalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/turnover-metrics")
@RequiredArgsConstructor
public class TurnoverController {

    private final AttritionSignalService service;

    @GetMapping
    public ResponseEntity<List<AttritionSignal>> findAll(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.findAll(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttritionSignal> findById(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.findById(id, tenantId));
    }

    @PostMapping
    public ResponseEntity<AttritionSignal> create(@RequestBody AttritionSignal entity, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.create(entity, tenantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttritionSignal> update(@PathVariable UUID id, @RequestBody AttritionSignal entity, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.update(id, entity, tenantId));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<ValidateResponse> validate(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.validate(id, tenantId));
    }
}
