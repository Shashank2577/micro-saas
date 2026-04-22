package com.microsaas.dependencyradar.controller;

import com.microsaas.dependencyradar.model.UpgradePR;
import com.microsaas.dependencyradar.service.UpgradePRService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/upgradeprs")
@RequiredArgsConstructor
public class UpgradePRController {
    private final UpgradePRService service;

    @GetMapping
    public ResponseEntity<List<UpgradePR>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UpgradePR> findById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UpgradePR> create(@RequestBody UpgradePR entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpgradePR> update(@PathVariable UUID id, @RequestBody UpgradePR entity) {
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
