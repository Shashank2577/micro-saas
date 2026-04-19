package com.microsaas.complianceradar.controller;

import com.microsaas.complianceradar.domain.JurisdictionRule;
import com.microsaas.complianceradar.service.NormalizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/regulations/jurisdiction-rules")
@RequiredArgsConstructor
public class JurisdictionRuleController {

    private final NormalizationService service;

    @GetMapping
    public ResponseEntity<List<JurisdictionRule>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JurisdictionRule> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<JurisdictionRule> create(@RequestBody JurisdictionRule rule) {
        return ResponseEntity.ok(service.create(rule));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<JurisdictionRule> update(@PathVariable UUID id, @RequestBody JurisdictionRule updateDetails) {
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
