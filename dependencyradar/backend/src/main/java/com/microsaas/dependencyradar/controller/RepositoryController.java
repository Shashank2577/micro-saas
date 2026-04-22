package com.microsaas.dependencyradar.controller;

import com.microsaas.dependencyradar.model.Repository;
import com.microsaas.dependencyradar.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/repositorys")
@RequiredArgsConstructor
public class RepositoryController {
    private final RepositoryService service;

    @GetMapping
    public ResponseEntity<List<Repository>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Repository> findById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Repository> create(@RequestBody Repository entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Repository> update(@PathVariable UUID id, @RequestBody Repository entity) {
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
