package com.microsaas.cashflowai.controller;

import com.microsaas.cashflowai.model.ShortfallAlert;
import com.microsaas.cashflowai.service.ShortfallAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cash-position/shortfall-alerts")
@RequiredArgsConstructor
public class ShortfallAlertController {
    private final ShortfallAlertService service;

    @GetMapping
    public ResponseEntity<List<ShortfallAlert>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShortfallAlert> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ShortfallAlert> create(@RequestBody ShortfallAlert entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ShortfallAlert> update(@PathVariable UUID id, @RequestBody ShortfallAlert entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.validate(id);
        return ResponseEntity.ok().build();
    }
}
