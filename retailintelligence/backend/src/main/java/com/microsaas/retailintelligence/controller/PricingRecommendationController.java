package com.microsaas.retailintelligence.controller;

import com.microsaas.retailintelligence.dto.PricingRecommendationDto;
import com.microsaas.retailintelligence.service.RetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pricing-recommendations")
@RequiredArgsConstructor
public class PricingRecommendationController {

    private final RetailService retailService;

    @GetMapping
    public ResponseEntity<List<PricingRecommendationDto>> getPendingRecommendations() {
        return ResponseEntity.ok(retailService.getPendingRecommendations());
    }

    @PostMapping("/generate")
    public ResponseEntity<Void> generatePricingRecommendations(@RequestParam UUID skuId) {
        retailService.generatePricingRecommendations(skuId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/apply")
    public ResponseEntity<Void> applyRecommendation(@PathVariable UUID id) {
        retailService.applyRecommendation(id);
        return ResponseEntity.ok().build();
    }
}
