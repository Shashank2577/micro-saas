package com.microsaas.financenarrator.controller;

import com.microsaas.financenarrator.model.ExportArtifact;
import com.microsaas.financenarrator.service.ExportArtifactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/narratives/export-artifacts")
@RequiredArgsConstructor
public class ExportArtifactController {
    private final ExportArtifactService service;

    @GetMapping
    public ResponseEntity<List<ExportArtifact>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping
    public ResponseEntity<ExportArtifact> create(@RequestBody ExportArtifact dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExportArtifact> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ExportArtifact> update(@PathVariable UUID id, @RequestBody ExportArtifact dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.validate(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/simulate")
    public ResponseEntity<Void> simulate(@PathVariable UUID id) {
        service.simulate(id);
        return ResponseEntity.ok().build();
    }
}
