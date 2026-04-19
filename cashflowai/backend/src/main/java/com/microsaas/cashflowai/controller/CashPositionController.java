package com.microsaas.cashflowai.controller;

import com.microsaas.cashflowai.model.CashPosition;
import com.microsaas.cashflowai.service.CashPositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cash-position/cash-positions")
@RequiredArgsConstructor
public class CashPositionController {
    private final CashPositionService service;

    @GetMapping
    public ResponseEntity<List<CashPosition>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CashPosition> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<CashPosition> create(@RequestBody CashPosition entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CashPosition> update(@PathVariable UUID id, @RequestBody CashPosition entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.validate(id);
        return ResponseEntity.ok().build();
    }
}
