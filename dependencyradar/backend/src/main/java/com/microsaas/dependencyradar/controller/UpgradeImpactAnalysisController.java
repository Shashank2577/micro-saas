package com.microsaas.dependencyradar.controller;

import com.microsaas.dependencyradar.model.UpgradeImpactAnalysis;
import com.microsaas.dependencyradar.service.UpgradeImpactAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/upgradeimpactanalysiss")
@RequiredArgsConstructor
public class UpgradeImpactAnalysisController {
    private final UpgradeImpactAnalysisService service;

    @GetMapping
    public ResponseEntity<List<UpgradeImpactAnalysis>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UpgradeImpactAnalysis> findById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UpgradeImpactAnalysis> create(@RequestBody UpgradeImpactAnalysis entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpgradeImpactAnalysis> update(@PathVariable UUID id, @RequestBody UpgradeImpactAnalysis entity) {
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
