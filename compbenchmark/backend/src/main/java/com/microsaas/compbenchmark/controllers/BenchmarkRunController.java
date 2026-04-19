package com.microsaas.compbenchmark.controllers;

import com.microsaas.compbenchmark.model.BenchmarkRun;
import com.microsaas.compbenchmark.services.BenchmarkRunService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/compensation-benchmark/benchmark-runs")
@RequiredArgsConstructor
public class BenchmarkRunController {
    private final BenchmarkRunService service;

    @GetMapping
    public List<BenchmarkRun> list() { return service.list(); }

    @PostMapping
    public BenchmarkRun create(@RequestBody BenchmarkRun entity) { return service.create(entity); }

    @GetMapping("/{id}")
    public BenchmarkRun getById(@PathVariable UUID id) { return service.getById(id); }

    @PatchMapping("/{id}")
    public BenchmarkRun update(@PathVariable UUID id, @RequestBody BenchmarkRun entity) { return service.update(id, entity); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) { service.delete(id); }

    @PostMapping("/{id}/validate")
    public Map<String, Object> validate(@PathVariable UUID id) { return service.validate(id); }

    @PostMapping("/{id}/simulate")
    public Map<String, Object> simulate(@PathVariable UUID id) { return service.simulate(id); }
}
