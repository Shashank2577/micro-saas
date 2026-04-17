package com.microsaas.pricingintelligence.service;

import com.microsaas.pricingintelligence.domain.*;
import com.microsaas.pricingintelligence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PricingIntelligenceService {

    private final HistoricalDataRepository historicalDataRepository;
    private final ElasticityModelRepository elasticityModelRepository;
    private final PricingExperimentRepository pricingExperimentRepository;
    private final ExperimentResultRepository experimentResultRepository;

    public List<ElasticityModel> getElasticityModels(UUID tenantId) {
        return elasticityModelRepository.findByTenantId(tenantId);
    }

    public List<PricingExperiment> getPricingExperiments(UUID tenantId) {
        return pricingExperimentRepository.findByTenantId(tenantId);
    }

    public ElasticityModel getElasticityModelBySegment(UUID tenantId, String segment) {
        return elasticityModelRepository.findByTenantIdAndSegment(tenantId, segment)
                .orElseThrow(() -> new RuntimeException("Model not found"));
    }

    public ElasticityModel generateElasticityModel(UUID tenantId, String segment) {
        List<HistoricalData> data = historicalDataRepository.findByTenantId(tenantId);

        // Mocking the Predictive ML (elasticity modeling) part
        BigDecimal basePrice = new BigDecimal("100.00");
        BigDecimal elasticityCoefficient = new BigDecimal("-1.5").setScale(4, RoundingMode.HALF_UP);
        BigDecimal confidenceScore = new BigDecimal("0.85").setScale(4, RoundingMode.HALF_UP);

        ElasticityModel model = new ElasticityModel();
        model.setId(UUID.randomUUID());
        model.setTenantId(tenantId);
        model.setSegment(segment);
        model.setBasePrice(basePrice);
        model.setElasticityCoefficient(elasticityCoefficient);
        model.setConfidenceScore(confidenceScore);
        model.setLastUpdatedAt(LocalDateTime.now());
        model.setCreatedAt(LocalDateTime.now());

        return elasticityModelRepository.save(model);
    }

    public PricingExperiment createExperiment(UUID tenantId, String name, String segment, BigDecimal controlPrice, BigDecimal variantPrice) {
        PricingExperiment experiment = new PricingExperiment();
        experiment.setId(UUID.randomUUID());
        experiment.setTenantId(tenantId);
        experiment.setName(name);
        experiment.setSegment(segment);
        experiment.setControlPrice(controlPrice);
        experiment.setVariantPrice(variantPrice);
        experiment.setStatus("DRAFT");
        experiment.setCreatedAt(LocalDateTime.now());

        return pricingExperimentRepository.save(experiment);
    }

    public ExperimentResult recordExperimentResult(UUID tenantId, UUID experimentId, String variantType, BigDecimal conversionRate, BigDecimal revenueImpact, Boolean isSignificant) {
        PricingExperiment experiment = pricingExperimentRepository.findById(experimentId)
            .orElseThrow(() -> new RuntimeException("Experiment not found"));

        if (!experiment.getTenantId().equals(tenantId)) {
            throw new RuntimeException("Unauthorized");
        }

        ExperimentResult result = new ExperimentResult();
        result.setId(UUID.randomUUID());
        result.setTenantId(tenantId);
        result.setExperimentId(experimentId);
        result.setVariantType(variantType);
        result.setConversionRate(conversionRate);
        result.setRevenueImpact(revenueImpact);
        result.setIsSignificant(isSignificant);
        result.setCreatedAt(LocalDateTime.now());

        return experimentResultRepository.save(result);
    }
}
