package com.microsaas.budgetmaster.controller;

import com.microsaas.budgetmaster.service.AiOptimizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/optimization")
@RequiredArgsConstructor
public class OptimizationController {
    private final AiOptimizationService aiOptimizationService;

    @GetMapping("/recommendations/{budgetId}")
    public ResponseEntity<Map<String, String>> getRecommendations(@PathVariable UUID budgetId) {
        String recommendations = aiOptimizationService.getOptimizationRecommendations(budgetId);
        return ResponseEntity.ok(Map.of("recommendations", recommendations));
    }
}
