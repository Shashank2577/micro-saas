package com.microsaas.revopsai.controller;

import com.microsaas.revopsai.model.ForecastAccuracy;
import com.microsaas.revopsai.service.ForecastAccuracyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/forecast-accuracy")
@RequiredArgsConstructor
public class ForecastAccuracyController {
    private final ForecastAccuracyService service;

    @GetMapping
    public ResponseEntity<List<ForecastAccuracy>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<ForecastAccuracy> create(@RequestBody ForecastAccuracy entity) {
        return ResponseEntity.ok(service.create(entity));
    }
}
