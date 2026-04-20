package com.microsaas.pricingintelligence.service;

import com.microsaas.pricingintelligence.domain.ElasticityModel;
import com.microsaas.pricingintelligence.repository.ElasticityModelRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WhatIfAnalysisService {

    private final ElasticityModelRepository elasticityModelRepository;

    public BigDecimal simulateRevenue(UUID segmentId, BigDecimal newPrice) {
        UUID tenantId = TenantContext.require();

        Optional<ElasticityModel> modelOpt = elasticityModelRepository.findByTenantIdAndSegmentId(tenantId, segmentId);
        if (modelOpt.isEmpty()) {
            throw new IllegalArgumentException("No elasticity model found to simulate against.");
        }

        ElasticityModel model = modelOpt.get();
        double elasticity = model.getElasticityCoefficient() != null ? model.getElasticityCoefficient() : -1.0;

        // Very basic simulation using elasticity logic (change in quantity / change in price)
        // Assume base price of $50 with baseline quantity 100
        BigDecimal basePrice = new BigDecimal("50.00");
        double percentPriceChange = newPrice.subtract(basePrice).doubleValue() / basePrice.doubleValue();
        double percentQuantityChange = elasticity * percentPriceChange;

        double newQuantity = 100 * (1 + percentQuantityChange);
        if (newQuantity < 0) newQuantity = 0;

        return newPrice.multiply(BigDecimal.valueOf(newQuantity));
    }
}
