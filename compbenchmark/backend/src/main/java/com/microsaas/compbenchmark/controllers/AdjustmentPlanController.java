package com.microsaas.compbenchmark.controllers;

import com.microsaas.compbenchmark.model.AdjustmentPlan;
import com.microsaas.compbenchmark.services.AdjustmentPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/compensation-benchmark/adjustment-plans")
@RequiredArgsConstructor
public class AdjustmentPlanController {
    private final AdjustmentPlanService service;

    @GetMapping
    public List<AdjustmentPlan> list() { return service.list(); }

    @PostMapping
    public AdjustmentPlan create(@RequestBody AdjustmentPlan entity) { return service.create(entity); }

    @GetMapping("/{id}")
    public AdjustmentPlan getById(@PathVariable UUID id) { return service.getById(id); }

    @PatchMapping("/{id}")
    public AdjustmentPlan update(@PathVariable UUID id, @RequestBody AdjustmentPlan entity) { return service.update(id, entity); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) { service.delete(id); }

    @PostMapping("/{id}/validate")
    public Map<String, Object> validate(@PathVariable UUID id) { return service.validate(id); }

    @PostMapping("/{id}/simulate")
    public Map<String, Object> simulate(@PathVariable UUID id) { return service.simulate(id); }
}
