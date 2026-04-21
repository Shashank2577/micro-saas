package com.microsaas.wealthedge.controller;

import com.microsaas.wealthedge.service.AIWealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai-wealth")
@RequiredArgsConstructor
public class AIWealthController {

    private final AIWealthService aiWealthService;

    @PostMapping("/analyze-risk")
    public ResponseEntity<String> analyzeRiskConcentration(@RequestBody String portfolioData) {
        String analysis = aiWealthService.analyzeRiskConcentration(portfolioData);
        return ResponseEntity.ok(analysis);
    }
}
