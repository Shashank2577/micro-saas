package com.microsaas.financenarrator.controller;

import com.microsaas.financenarrator.model.NarrativeRequest;
import com.microsaas.financenarrator.service.NarrativeRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/narratives/narrative-requests")
@RequiredArgsConstructor
public class NarrativeRequestController {
    private final NarrativeRequestService service;

    @GetMapping
    public ResponseEntity<List<NarrativeRequest>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping
    public ResponseEntity<NarrativeRequest> create(@RequestBody NarrativeRequest dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NarrativeRequest> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<NarrativeRequest> update(@PathVariable UUID id, @RequestBody NarrativeRequest dto) {
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
