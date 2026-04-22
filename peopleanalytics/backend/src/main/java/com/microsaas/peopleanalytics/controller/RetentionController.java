package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.model.RetentionPrediction;
import com.microsaas.peopleanalytics.service.RetentionPredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/people-analytics/retention")
@RequiredArgsConstructor
public class RetentionController {
    private final RetentionPredictionService retentionPredictionService;

    @GetMapping("/risk")
    public List<RetentionPrediction> getHighRisk() {
        return retentionPredictionService.getHighRiskEmployees();
    }

    @PostMapping("/predict")
    public void triggerPredictions() {
        retentionPredictionService.generatePredictions();
    }

    @GetMapping("/forecasting")
    public ResponseEntity<Map<String, Object>> getTurnoverForecasting() {
        // Implementation for turnover forecasting placeholder
        return ResponseEntity.ok(Map.of(
            "probability", 0.15,
            "projectedTurnover", 12,
            "period", "Next 6 Months"
        ));
    }
}
