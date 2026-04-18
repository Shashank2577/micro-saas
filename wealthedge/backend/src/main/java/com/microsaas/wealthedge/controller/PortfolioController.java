package com.microsaas.wealthedge.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.wealthedge.domain.Portfolio;
import com.microsaas.wealthedge.service.AIWealthService;
import com.microsaas.wealthedge.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final AIWealthService aiWealthService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public List<Portfolio> getAllPortfolios() {
        return portfolioService.getAllPortfolios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Portfolio> getPortfolio(@PathVariable UUID id) {
        return ResponseEntity.ok(portfolioService.getPortfolio(id));
    }

    @PostMapping
    public ResponseEntity<Portfolio> createPortfolio(@RequestBody Portfolio portfolio) {
        return ResponseEntity.ok(portfolioService.createPortfolio(portfolio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Portfolio> updatePortfolio(@PathVariable UUID id, @RequestBody Portfolio portfolio) {
        return ResponseEntity.ok(portfolioService.updatePortfolio(id, portfolio));
    }

    @PostMapping("/{id}/assets/{assetId}")
    public ResponseEntity<Portfolio> addAsset(@PathVariable UUID id, @PathVariable UUID assetId) {
        return ResponseEntity.ok(portfolioService.addAssetToPortfolio(id, assetId));
    }

    @DeleteMapping("/{id}/assets/{assetId}")
    public ResponseEntity<Portfolio> removeAsset(@PathVariable UUID id, @PathVariable UUID assetId) {
        return ResponseEntity.ok(portfolioService.removeAssetFromPortfolio(id, assetId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable UUID id) {
        portfolioService.deletePortfolio(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/insights")
    public ResponseEntity<Map<String, String>> getInsights(@PathVariable UUID id) throws JsonProcessingException {
        Portfolio portfolio = portfolioService.getPortfolio(id);
        String data = objectMapper.writeValueAsString(portfolio);
        String insights = aiWealthService.analyzeRiskConcentration(data);
        return ResponseEntity.ok(Map.of("insights", insights));
    }
}
