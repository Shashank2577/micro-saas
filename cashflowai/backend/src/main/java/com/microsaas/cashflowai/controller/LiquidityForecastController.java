package com.microsaas.cashflowai.controller;

import com.microsaas.cashflowai.model.LiquidityForecast;
import com.microsaas.cashflowai.service.LiquidityForecastService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cash-position/liquidity-forecasts")
@RequiredArgsConstructor
public class LiquidityForecastController {
    private final LiquidityForecastService service;

    @GetMapping
    public ResponseEntity<List<LiquidityForecast>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LiquidityForecast> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<LiquidityForecast> create(@RequestBody LiquidityForecast entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LiquidityForecast> update(@PathVariable UUID id, @RequestBody LiquidityForecast entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.validate(id);
        return ResponseEntity.ok().build();
    }
}
