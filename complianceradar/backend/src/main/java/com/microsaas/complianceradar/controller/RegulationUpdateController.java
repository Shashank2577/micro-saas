package com.microsaas.complianceradar.controller;

import com.microsaas.complianceradar.domain.RegulationUpdate;
import com.microsaas.complianceradar.service.FeedsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/regulations/regulation-updates")
@RequiredArgsConstructor
public class RegulationUpdateController {

    private final FeedsService service;

    @GetMapping
    public ResponseEntity<List<RegulationUpdate>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegulationUpdate> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<RegulationUpdate> create(@RequestBody RegulationUpdate update) {
        return ResponseEntity.ok(service.create(update));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RegulationUpdate> update(@PathVariable UUID id, @RequestBody RegulationUpdate updateDetails) {
        return ResponseEntity.ok(service.update(id, updateDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.validate(id);
        return ResponseEntity.ok().build();
    }
}
