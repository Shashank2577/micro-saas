package com.microsaas.marketsignal.controller;

import com.microsaas.marketsignal.domain.entity.MarketPattern;
import com.microsaas.marketsignal.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping("/detect")
    public ResponseEntity<MarketPattern> detectPatterns() {
        MarketPattern pattern = analysisService.detectPatterns();
        return ResponseEntity.ok(pattern);
    }
}
