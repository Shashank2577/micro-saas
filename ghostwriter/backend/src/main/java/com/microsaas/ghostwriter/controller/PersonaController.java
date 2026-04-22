package com.microsaas.ghostwriter.controller;

import com.microsaas.ghostwriter.model.Persona;
import com.microsaas.ghostwriter.service.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
public class PersonaController {
    private final PersonaService service;

    @GetMapping
    public ResponseEntity<List<Persona>> getAll(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(service.getAll(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> getById(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") String tenantId) {
        return service.getById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Persona> create(@RequestBody Persona persona, @RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(service.create(persona, tenantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Persona> update(@PathVariable UUID id, @RequestBody Persona persona, @RequestHeader("X-Tenant-ID") String tenantId) {
        try {
            return ResponseEntity.ok(service.update(id, persona, tenantId));
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
