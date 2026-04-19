package com.microsaas.cashflowanalyzer.controller;

import com.microsaas.cashflowanalyzer.service.AiAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cashflow/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiAnalysisService service;

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, String>> analyze(@RequestBody Map<String, String> request) {
        String context = request.getOrDefault("context", "");
        String result = service.analyze(context);
        Map<String, String> response = new HashMap<>();
        response.put("result", result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recommendations")
    public ResponseEntity<Map<String, List<String>>> recommendations(@RequestBody Map<String, String> request) {
        String context = request.getOrDefault("context", "");
        List<String> recs = service.getRecommendations(context);
        Map<String, List<String>> response = new HashMap<>();
        response.put("recommendations", recs);
        return ResponseEntity.ok(response);
    }
}
