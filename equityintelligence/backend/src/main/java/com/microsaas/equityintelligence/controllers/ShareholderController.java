package com.microsaas.equityintelligence.controllers;

import com.microsaas.equityintelligence.model.Shareholder;
import com.microsaas.equityintelligence.services.ShareholderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/equity/shareholders")
public class ShareholderController {

    private final ShareholderService service;

    public ShareholderController(ShareholderService service) {
        this.service = service;
    }

    @GetMapping
    public List<Shareholder> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shareholder> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Shareholder create(@RequestBody Shareholder entity) {
        return service.create(entity);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Shareholder> update(@PathVariable UUID id, @RequestBody Shareholder entity) {
        try {
            return ResponseEntity.ok(service.update(id, entity));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        try {
            service.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Boolean> validate(@PathVariable UUID id) {
        return ResponseEntity.ok(service.validate(id));
    }

    @PostMapping("/{id}/simulate")
    public ResponseEntity<String> simulate(@PathVariable UUID id) {
        return ResponseEntity.ok(service.simulate(id));
    }
}
