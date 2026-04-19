package com.microsaas.cashflowanalyzer.controller;

import com.microsaas.cashflowanalyzer.model.AnomalyFlag;
import com.microsaas.cashflowanalyzer.service.AnomaliesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/cashflow/anomaly-flags")
@RequiredArgsConstructor
public class AnomalyFlagController {

    private final AnomaliesService service;

    @GetMapping
    public ResponseEntity<List<AnomalyFlag>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping
    public ResponseEntity<AnomalyFlag> create(@RequestBody AnomalyFlag flag) {
        return ResponseEntity.ok(service.create(flag));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnomalyFlag> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AnomalyFlag> update(@PathVariable UUID id, @RequestBody AnomalyFlag flag) {
        return ResponseEntity.ok(service.update(id, flag));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, Boolean>> validate(@PathVariable UUID id) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", service.validate(id));
        return ResponseEntity.ok(response);
    }
}
