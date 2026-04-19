package com.microsaas.cashflowanalyzer.controller;

import com.microsaas.cashflowanalyzer.model.CashMovement;
import com.microsaas.cashflowanalyzer.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/cashflow/cash-movements")
@RequiredArgsConstructor
public class CashMovementController {

    private final AnalysisService service;

    @GetMapping
    public ResponseEntity<List<CashMovement>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping
    public ResponseEntity<CashMovement> create(@RequestBody CashMovement movement) {
        return ResponseEntity.ok(service.create(movement));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CashMovement> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CashMovement> update(@PathVariable UUID id, @RequestBody CashMovement movement) {
        return ResponseEntity.ok(service.update(id, movement));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, Boolean>> validate(@PathVariable UUID id) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", service.validate(id));
        return ResponseEntity.ok(response);
    }
}
