package com.microsaas.dependencyradar.controller;

import com.microsaas.dependencyradar.model.ScanJob;
import com.microsaas.dependencyradar.service.ScanJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/scanjobs")
@RequiredArgsConstructor
public class ScanJobController {
    private final ScanJobService service;

    @GetMapping
    public ResponseEntity<List<ScanJob>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScanJob> findById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ScanJob> create(@RequestBody ScanJob entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScanJob> update(@PathVariable UUID id, @RequestBody ScanJob entity) {
        return service.findById(id)
                .map(existing -> {
                    entity.setId(id);
                    entity.setTenantId(existing.getTenantId());
                    return ResponseEntity.ok(service.save(entity));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
