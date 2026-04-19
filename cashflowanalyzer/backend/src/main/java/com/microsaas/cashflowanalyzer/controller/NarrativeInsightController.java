package com.microsaas.cashflowanalyzer.controller;

import com.microsaas.cashflowanalyzer.model.NarrativeInsight;
import com.microsaas.cashflowanalyzer.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/cashflow/narrative-insights")
@RequiredArgsConstructor
public class NarrativeInsightController {

    private final ReportingService service;

    @GetMapping
    public ResponseEntity<List<NarrativeInsight>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping
    public ResponseEntity<NarrativeInsight> create(@RequestBody NarrativeInsight insight) {
        return ResponseEntity.ok(service.create(insight));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NarrativeInsight> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<NarrativeInsight> update(@PathVariable UUID id, @RequestBody NarrativeInsight insight) {
        return ResponseEntity.ok(service.update(id, insight));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, Boolean>> validate(@PathVariable UUID id) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", service.validate(id));
        return ResponseEntity.ok(response);
    }
}
