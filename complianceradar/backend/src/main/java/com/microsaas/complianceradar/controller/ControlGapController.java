package com.microsaas.complianceradar.controller;

import com.microsaas.complianceradar.domain.ControlGap;
import com.microsaas.complianceradar.service.GapMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/regulations/control-gaps")
@RequiredArgsConstructor
public class ControlGapController {

    private final GapMappingService service;

    @GetMapping
    public ResponseEntity<List<ControlGap>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ControlGap> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ControlGap> create(@RequestBody ControlGap gap) {
        return ResponseEntity.ok(service.create(gap));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ControlGap> update(@PathVariable UUID id, @RequestBody ControlGap updateDetails) {
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
