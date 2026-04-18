package com.microsaas.debtnavigator.controller;

import com.microsaas.debtnavigator.service.DebtAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/debt/ai")
@RequiredArgsConstructor
public class AiController {
    private final DebtAiService aiService;

    @PostMapping("/analyze")
    public ResponseEntity<String> analyze(@RequestBody String prompt) {
        return ResponseEntity.ok(aiService.analyzeDebt(prompt));
    }

    @PostMapping("/recommendations")
    public ResponseEntity<String> recommendations(@RequestBody String prompt) {
        return ResponseEntity.ok(aiService.getRecommendations(prompt));
    }
}
