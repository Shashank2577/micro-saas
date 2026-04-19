package com.microsaas.cashflowanalyzer.controller;

import com.microsaas.cashflowanalyzer.model.ForecastRun;
import com.microsaas.cashflowanalyzer.service.ForecastingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/cashflow/forecast-runs")
@RequiredArgsConstructor
public class ForecastRunController {

    private final ForecastingService service;

    @GetMapping
    public ResponseEntity<List<ForecastRun>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping
    public ResponseEntity<ForecastRun> create(@RequestBody ForecastRun run) {
        return ResponseEntity.ok(service.create(run));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ForecastRun> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ForecastRun> update(@PathVariable UUID id, @RequestBody ForecastRun run) {
        return ResponseEntity.ok(service.update(id, run));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, Boolean>> validate(@PathVariable UUID id) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", service.validate(id));
        return ResponseEntity.ok(response);
    }
}
