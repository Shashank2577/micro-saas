package com.microsaas.compbenchmark.controllers;

import com.microsaas.compbenchmark.model.CompBand;
import com.microsaas.compbenchmark.services.CompBandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/compensation-benchmark/comp-bands")
@RequiredArgsConstructor
public class CompBandController {
    private final CompBandService service;

    @GetMapping
    public List<CompBand> list() {
        return service.list();
    }

    @PostMapping
    public CompBand create(@RequestBody CompBand band) {
        return service.create(band);
    }

    @GetMapping("/{id}")
    public CompBand getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PatchMapping("/{id}")
    public CompBand update(@PathVariable UUID id, @RequestBody CompBand band) {
        return service.update(id, band);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @PostMapping("/{id}/validate")
    public Map<String, Object> validate(@PathVariable UUID id) {
        return service.validate(id);
    }

    @PostMapping("/{id}/simulate")
    public Map<String, Object> simulate(@PathVariable UUID id) {
        return service.simulate(id);
    }
}
