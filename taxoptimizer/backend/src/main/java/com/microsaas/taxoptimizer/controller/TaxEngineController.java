package com.microsaas.taxoptimizer.controller;

import com.microsaas.taxoptimizer.domain.entity.TaxCalculation;
import com.microsaas.taxoptimizer.domain.entity.TaxProfile;
import com.microsaas.taxoptimizer.service.OptimizationService;
import com.microsaas.taxoptimizer.service.TaxLiabilityService;
import com.microsaas.taxoptimizer.service.TaxLossHarvestingService;
import com.microsaas.taxoptimizer.service.TaxProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tax-engine")
@RequiredArgsConstructor
public class TaxEngineController {

    private final TaxLiabilityService taxLiabilityService;
    private final TaxLossHarvestingService taxLossHarvestingService;
    private final OptimizationService optimizationService;
    private final TaxProfileService taxProfileService;

    @PostMapping("/calculate-liability")
    public ResponseEntity<TaxCalculation> calculateLiability(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestParam UUID userId,
            @RequestParam Integer taxYear,
            @RequestParam String type) {
        TaxProfile profile = taxProfileService.getProfile(tenantId, userId).orElseThrow();
        return ResponseEntity.ok(taxLiabilityService.calculateLiability(tenantId, profile, taxYear, type));
    }

    @GetMapping("/harvesting-opportunities")
    public ResponseEntity<?> getHarvestingOpportunities(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestParam UUID userId,
            @RequestParam Integer taxYear) {
        TaxProfile profile = taxProfileService.getProfile(tenantId, userId).orElseThrow();
        return ResponseEntity.ok(taxLossHarvestingService.identifyOpportunities(tenantId, profile.getId(), taxYear));
    }

    @GetMapping("/optimize-charitable")
    public ResponseEntity<?> optimizeCharitableGiving(
            @RequestParam BigDecimal income) {
        return ResponseEntity.ok(optimizationService.optimizeCharitableGiving(income));
    }

    @PostMapping("/model-entity")
    public ResponseEntity<?> modelEntityStructure(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestParam UUID userId,
            @RequestParam String targetEntity) {
        TaxProfile profile = taxProfileService.getProfile(tenantId, userId).orElseThrow();
        return ResponseEntity.ok(optimizationService.modelEntityStructure(tenantId, profile, targetEntity));
    }
}
