package com.microsaas.ghostwriter.controller;

import com.microsaas.ghostwriter.model.Template;
import com.microsaas.ghostwriter.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplateController {
    private final TemplateService service;

    @GetMapping
    public ResponseEntity<List<Template>> getAll(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(service.getAll(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Template> getById(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") String tenantId) {
        return service.getById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Template> create(@RequestBody Template template, @RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(service.create(template, tenantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Template> update(@PathVariable UUID id, @RequestBody Template template, @RequestHeader("X-Tenant-ID") String tenantId) {
        try {
            return ResponseEntity.ok(service.update(id, template, tenantId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") String tenantId) {
        service.delete(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}
