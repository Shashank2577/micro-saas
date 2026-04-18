package com.microsaas.investtracker.controller;

import com.microsaas.investtracker.dto.AiInsightDto;
import com.microsaas.investtracker.dto.CreatePortfolioRequest;
import com.microsaas.investtracker.dto.PortfolioDto;
import com.microsaas.investtracker.service.AiOptimizationService;
import com.microsaas.investtracker.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/portfolios")
@RequiredArgsConstructor
public class PortfolioController {
    private final PortfolioService portfolioService;
    private final AiOptimizationService aiOptimizationService;

    @GetMapping
    public ResponseEntity<List<PortfolioDto>> getAllPortfolios() {
        return ResponseEntity.ok(portfolioService.getAllPortfolios());
    }

    @PostMapping
    public ResponseEntity<PortfolioDto> createPortfolio(@RequestBody CreatePortfolioRequest request) {
        return ResponseEntity.ok(portfolioService.createPortfolio(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioDto> getPortfolio(@PathVariable UUID id) {
        return ResponseEntity.ok(portfolioService.getPortfolio(id));
    }

    @GetMapping("/{id}/ai-insights")
    public ResponseEntity<AiInsightDto> getAiInsights(@PathVariable UUID id) {
        return ResponseEntity.ok(aiOptimizationService.generateInsights(id));
    }
}
