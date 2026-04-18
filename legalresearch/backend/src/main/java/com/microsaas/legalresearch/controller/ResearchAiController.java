package com.microsaas.legalresearch.controller;

import com.microsaas.legalresearch.service.LegalResearchAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/research/ai")
@RequiredArgsConstructor
public class ResearchAiController {
    private final LegalResearchAiService aiService;

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, String>> analyze(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(Map.of("result", aiService.analyze(payload)));
    }

    @PostMapping("/recommendations")
    public ResponseEntity<Map<String, String>> recommendations(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(Map.of("result", aiService.recommendations(payload)));
    }
}
