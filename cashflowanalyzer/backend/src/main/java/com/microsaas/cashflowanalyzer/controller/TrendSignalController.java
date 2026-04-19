package com.microsaas.cashflowanalyzer.controller;

import com.microsaas.cashflowanalyzer.model.TrendSignal;
import com.microsaas.cashflowanalyzer.service.InsightsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/cashflow/trend-signals")
@RequiredArgsConstructor
public class TrendSignalController {

    private final InsightsService service;

    @GetMapping
    public ResponseEntity<List<TrendSignal>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping
    public ResponseEntity<TrendSignal> create(@RequestBody TrendSignal signal) {
        return ResponseEntity.ok(service.create(signal));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrendSignal> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TrendSignal> update(@PathVariable UUID id, @RequestBody TrendSignal signal) {
        return ResponseEntity.ok(service.update(id, signal));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, Boolean>> validate(@PathVariable UUID id) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", service.validate(id));
        return ResponseEntity.ok(response);
    }
}
