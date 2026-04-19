package com.microsaas.compbenchmark.controllers;

import com.microsaas.compbenchmark.model.GapFinding;
import com.microsaas.compbenchmark.services.GapFindingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/compensation-benchmark/gap-findings")
@RequiredArgsConstructor
public class GapFindingController {
    private final GapFindingService service;

    @GetMapping
    public List<GapFinding> list() { return service.list(); }

    @PostMapping
    public GapFinding create(@RequestBody GapFinding entity) { return service.create(entity); }

    @GetMapping("/{id}")
    public GapFinding getById(@PathVariable UUID id) { return service.getById(id); }

    @PatchMapping("/{id}")
    public GapFinding update(@PathVariable UUID id, @RequestBody GapFinding entity) { return service.update(id, entity); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) { service.delete(id); }

    @PostMapping("/{id}/validate")
    public Map<String, Object> validate(@PathVariable UUID id) { return service.validate(id); }

    @PostMapping("/{id}/simulate")
    public Map<String, Object> simulate(@PathVariable UUID id) { return service.simulate(id); }
}
