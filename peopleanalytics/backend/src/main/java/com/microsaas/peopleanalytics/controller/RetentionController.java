package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.model.RetentionPrediction;
import com.microsaas.peopleanalytics.service.RetentionPredictionService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/retention")
@RequiredArgsConstructor
public class RetentionController {

    private final RetentionPredictionService service;

    @GetMapping("/risks")
    public ResponseEntity<List<RetentionPrediction>> getRisks(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.getRisks(tenantId));
    }

    @PostMapping("/predict")
    public ResponseEntity<Void> predictRisks(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        service.predictRisks(tenantId);
        return ResponseEntity.ok().build();
    }
}
