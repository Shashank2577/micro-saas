package com.microsaas.revopsai.controller;

import com.microsaas.revopsai.model.CacCalculation;
import com.microsaas.revopsai.service.CacCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cac-calculations")
@RequiredArgsConstructor
public class CacCalculationController {
    private final CacCalculationService service;

    @GetMapping
    public ResponseEntity<List<CacCalculation>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<CacCalculation> create(@RequestBody CacCalculation entity) {
        return ResponseEntity.ok(service.create(entity));
    }
}
