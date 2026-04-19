package com.microsaas.cashflowai.controller;

import com.microsaas.cashflowai.model.MitigationOption;
import com.microsaas.cashflowai.service.MitigationOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cash-position/mitigation-options")
@RequiredArgsConstructor
public class MitigationOptionController {
    private final MitigationOptionService service;

    @GetMapping
    public ResponseEntity<List<MitigationOption>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MitigationOption> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<MitigationOption> create(@RequestBody MitigationOption entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MitigationOption> update(@PathVariable UUID id, @RequestBody MitigationOption entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.validate(id);
        return ResponseEntity.ok().build();
    }
}
