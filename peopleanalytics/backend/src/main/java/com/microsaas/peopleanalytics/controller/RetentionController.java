package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.model.RetentionPrediction;
import com.microsaas.peopleanalytics.service.RetentionPredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/people-analytics/retention")
@RequiredArgsConstructor
public class RetentionController {
    private final RetentionPredictionService retentionPredictionService;

    @GetMapping("/risk")
    @PreAuthorize("hasAnyRole('HR', 'EXECUTIVE')")
    public List<RetentionPrediction> getHighRisk() {
        return retentionPredictionService.getHighRiskEmployees();
    }

    @PostMapping("/predict")
    @PreAuthorize("hasRole('HR')")
    public void triggerPredictions() {
        retentionPredictionService.generatePredictions();
    }
}
