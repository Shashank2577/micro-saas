package com.microsaas.cashflowai.controller;

import com.microsaas.cashflowai.model.ScenarioRun;
import com.microsaas.cashflowai.service.ScenarioRunService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cash-position/scenario-runs")
@RequiredArgsConstructor
public class ScenarioRunController {
    private final ScenarioRunService service;

    @GetMapping
    public ResponseEntity<List<ScenarioRun>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScenarioRun> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ScenarioRun> create(@RequestBody ScenarioRun entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScenarioRun> update(@PathVariable UUID id, @RequestBody ScenarioRun entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.validate(id);
        return ResponseEntity.ok().build();
    }
}
