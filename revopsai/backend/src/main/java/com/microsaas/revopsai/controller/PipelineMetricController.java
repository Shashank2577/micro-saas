package com.microsaas.revopsai.controller;

import com.microsaas.revopsai.model.PipelineMetric;
import com.microsaas.revopsai.service.PipelineMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pipeline-metrics")
@RequiredArgsConstructor
public class PipelineMetricController {
    private final PipelineMetricService service;

    @GetMapping
    public ResponseEntity<List<PipelineMetric>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<PipelineMetric> create(@RequestBody PipelineMetric entity) {
        return ResponseEntity.ok(service.create(entity));
    }
}
