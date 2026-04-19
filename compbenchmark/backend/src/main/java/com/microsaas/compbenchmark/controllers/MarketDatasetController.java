package com.microsaas.compbenchmark.controllers;

import com.microsaas.compbenchmark.model.MarketDataset;
import com.microsaas.compbenchmark.services.MarketDatasetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/compensation-benchmark/market-datasets")
@RequiredArgsConstructor
public class MarketDatasetController {
    private final MarketDatasetService service;

    @GetMapping
    public List<MarketDataset> list() { return service.list(); }

    @PostMapping
    public MarketDataset create(@RequestBody MarketDataset entity) { return service.create(entity); }

    @GetMapping("/{id}")
    public MarketDataset getById(@PathVariable UUID id) { return service.getById(id); }

    @PatchMapping("/{id}")
    public MarketDataset update(@PathVariable UUID id, @RequestBody MarketDataset entity) { return service.update(id, entity); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) { service.delete(id); }

    @PostMapping("/{id}/validate")
    public Map<String, Object> validate(@PathVariable UUID id) { return service.validate(id); }

    @PostMapping("/{id}/simulate")
    public Map<String, Object> simulate(@PathVariable UUID id) { return service.simulate(id); }
}
