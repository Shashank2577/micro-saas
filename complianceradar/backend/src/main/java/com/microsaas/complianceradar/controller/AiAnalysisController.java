package com.microsaas.complianceradar.controller;

import com.microsaas.complianceradar.service.NormalizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/regulations/ai")
@RequiredArgsConstructor
public class AiAnalysisController {

    private final NormalizationService normalizationService;

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, String>> analyze(@RequestBody Map<String, String> request) {
        String analysis = normalizationService.analyzeText(request.get("text"));
        return ResponseEntity.ok(Map.of("result", analysis));
    }

    @PostMapping("/recommendations")
    public ResponseEntity<Map<String, String>> recommendations(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(Map.of("result", "AI Recommendations for: " + request.get("text")));
    }
}
