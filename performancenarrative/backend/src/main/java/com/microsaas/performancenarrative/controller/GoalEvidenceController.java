package com.microsaas.performancenarrative.controller;

import com.microsaas.performancenarrative.entity.GoalEvidence;
import com.microsaas.performancenarrative.service.GoalEvidenceService;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/v1/performance/goal-evidences")
public class GoalEvidenceController {

    private final GoalEvidenceService service;

    public GoalEvidenceController(GoalEvidenceService service) {
        this.service = service;
    }

    @GetMapping
    public List<GoalEvidence> list() {
        return service.list();
    }

    @PostMapping
    public GoalEvidence create(@RequestBody GoalEvidence entity) {
        return service.create(entity);
    }

    @GetMapping("/{id}")
    public GoalEvidence getById(@PathVariable UUID id) {
        return service.getById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @PatchMapping("/{id}")
    public GoalEvidence update(@PathVariable UUID id, @RequestBody GoalEvidence details) {
        return service.update(id, details);
    }

    @PostMapping("/{id}/validate")
    public boolean validate(@PathVariable UUID id) {
        return service.validate(id);
    }
}
