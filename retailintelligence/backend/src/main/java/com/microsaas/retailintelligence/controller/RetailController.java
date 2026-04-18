package com.microsaas.retailintelligence.controller;

import com.microsaas.retailintelligence.dto.*;
import com.microsaas.retailintelligence.service.RetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RetailController {

    private final RetailService retailService;

    public RetailController(RetailService retailService) {
        this.retailService = retailService;
    }

    @GetMapping("/skus")
    public ResponseEntity<List<SkuDto>> getSkus() {
        return ResponseEntity.ok(retailService.getSkus());
    }

    @PostMapping("/skus")
    public ResponseEntity<SkuDto> createSku(@RequestBody CreateSkuRequest request) {
        return ResponseEntity.ok(retailService.createSku(request));
    }

    @GetMapping("/skus/{id}")
    public ResponseEntity<SkuDto> getSku(@PathVariable UUID id) {
        return ResponseEntity.ok(retailService.getSku(id));
    }

    @GetMapping("/forecasts")
    public ResponseEntity<List<DemandForecastDto>> getForecasts(@RequestParam UUID skuId) {
        return ResponseEntity.ok(retailService.getForecasts(skuId));
    }

    @PostMapping("/forecasts/generate")
    public ResponseEntity<Void> generateForecast(@RequestParam UUID skuId) {
        retailService.generateForecast(skuId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pricing-recommendations")
    public ResponseEntity<List<PricingRecommendationDto>> getPendingRecommendations() {
        return ResponseEntity.ok(retailService.getPendingRecommendations());
    }

    @PostMapping("/pricing-recommendations/generate")
    public ResponseEntity<Void> generatePricingRecommendations(@RequestParam UUID skuId) {
        retailService.generatePricingRecommendations(skuId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/pricing-recommendations/{id}/apply")
    public ResponseEntity<Void> applyRecommendation(@PathVariable UUID id) {
        retailService.applyRecommendation(id);
        return ResponseEntity.ok().build();
    }
}
