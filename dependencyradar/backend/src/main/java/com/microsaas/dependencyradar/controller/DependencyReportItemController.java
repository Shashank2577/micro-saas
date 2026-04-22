package com.microsaas.dependencyradar.controller;

import com.microsaas.dependencyradar.model.DependencyReportItem;
import com.microsaas.dependencyradar.service.DependencyReportItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dependencyreportitems")
@RequiredArgsConstructor
public class DependencyReportItemController {
    private final DependencyReportItemService service;

    @GetMapping
    public ResponseEntity<List<DependencyReportItem>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DependencyReportItem> findById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DependencyReportItem> create(@RequestBody DependencyReportItem entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DependencyReportItem> update(@PathVariable UUID id, @RequestBody DependencyReportItem entity) {
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
