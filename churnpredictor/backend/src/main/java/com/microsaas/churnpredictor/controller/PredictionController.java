package com.microsaas.churnpredictor.controller;

import com.microsaas.churnpredictor.dto.ChurnPredictionDto;
import com.microsaas.churnpredictor.service.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/predictions")
@RequiredArgsConstructor
public class PredictionController {
    private final PredictionService predictionService;

    @GetMapping
    public ResponseEntity<List<ChurnPredictionDto>> getLatestPredictions() {
        return ResponseEntity.ok(predictionService.getLatestPredictions());
    }

    @PostMapping("/recalculate")
    public ResponseEntity<Void> recalculatePredictions() {
        predictionService.recalculatePredictions();
        return ResponseEntity.ok().build();
    }
}
