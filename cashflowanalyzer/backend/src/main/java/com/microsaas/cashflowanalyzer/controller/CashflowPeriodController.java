package com.microsaas.cashflowanalyzer.controller;

import com.microsaas.cashflowanalyzer.model.CashflowPeriod;
import com.microsaas.cashflowanalyzer.service.IngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/cashflow/cashflow-periods")
@RequiredArgsConstructor
public class CashflowPeriodController {

    private final IngestionService service;

    @GetMapping
    public ResponseEntity<List<CashflowPeriod>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping
    public ResponseEntity<CashflowPeriod> create(@RequestBody CashflowPeriod period) {
        return ResponseEntity.ok(service.create(period));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CashflowPeriod> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CashflowPeriod> update(@PathVariable UUID id, @RequestBody CashflowPeriod period) {
        return ResponseEntity.ok(service.update(id, period));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, Boolean>> validate(@PathVariable UUID id) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", service.validate(id));
        return ResponseEntity.ok(response);
    }
}
