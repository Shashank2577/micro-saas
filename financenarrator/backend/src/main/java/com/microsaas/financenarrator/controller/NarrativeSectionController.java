package com.microsaas.financenarrator.controller;

import com.microsaas.financenarrator.model.NarrativeSection;
import com.microsaas.financenarrator.service.NarrativeSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/narratives/narrative-sections")
@RequiredArgsConstructor
public class NarrativeSectionController {
    private final NarrativeSectionService service;

    @GetMapping
    public ResponseEntity<List<NarrativeSection>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping
    public ResponseEntity<NarrativeSection> create(@RequestBody NarrativeSection dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NarrativeSection> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<NarrativeSection> update(@PathVariable UUID id, @RequestBody NarrativeSection dto) {
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
