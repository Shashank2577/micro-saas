package com.microsaas.pricingintelligence.api;

import com.microsaas.pricingintelligence.domain.*;
import com.microsaas.pricingintelligence.service.PricingIntelligenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pricingintelligence")
@RequiredArgsConstructor
public class PricingIntelligenceController {

    private final PricingIntelligenceService pricingIntelligenceService;

    @GetMapping("/models")
    public List<ElasticityModel> getModels(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return pricingIntelligenceService.getElasticityModels(tenantId);
    }

    @GetMapping("/models/{segment}")
    public ElasticityModel getModelBySegment(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable String segment) {
        return pricingIntelligenceService.getElasticityModelBySegment(tenantId, segment);
    }

    @PostMapping("/models/generate")
    public ElasticityModel generateModel(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestParam String segment) {
        return pricingIntelligenceService.generateElasticityModel(tenantId, segment);
    }

    @GetMapping("/experiments")
    public List<PricingExperiment> getExperiments(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return pricingIntelligenceService.getPricingExperiments(tenantId);
    }

    @PostMapping("/experiments")
    public PricingExperiment createExperiment(@RequestHeader("X-Tenant-ID") UUID tenantId,
                                              @RequestParam String name,
                                              @RequestParam String segment,
                                              @RequestParam BigDecimal controlPrice,
                                              @RequestParam BigDecimal variantPrice) {
        return pricingIntelligenceService.createExperiment(tenantId, name, segment, controlPrice, variantPrice);
    }

    @PostMapping("/experiments/{experimentId}/results")
    public ExperimentResult recordResult(@RequestHeader("X-Tenant-ID") UUID tenantId,
                                         @PathVariable UUID experimentId,
                                         @RequestParam String variantType,
                                         @RequestParam BigDecimal conversionRate,
                                         @RequestParam BigDecimal revenueImpact,
                                         @RequestParam Boolean isSignificant) {
        return pricingIntelligenceService.recordExperimentResult(tenantId, experimentId, variantType, conversionRate, revenueImpact, isSignificant);
    }
}
