package com.microsaas.dependencyradar.controller;

import com.microsaas.dependencyradar.model.Dependency;
import com.microsaas.dependencyradar.service.DependencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dependencys")
@RequiredArgsConstructor
public class DependencyController {
    private final DependencyService service;

    @GetMapping
    public ResponseEntity<List<Dependency>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dependency> findById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Dependency> create(@RequestBody Dependency entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dependency> update(@PathVariable UUID id, @RequestBody Dependency entity) {
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
