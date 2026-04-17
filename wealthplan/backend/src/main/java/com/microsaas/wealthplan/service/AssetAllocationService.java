package com.microsaas.wealthplan.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.wealthplan.entity.AllocationRecommendation;
import com.microsaas.wealthplan.repository.AllocationRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AssetAllocationService {
    private final AllocationRecommendationRepository recommendationRepository;

    @Transactional
    public AllocationRecommendation optimizeAllocation(int age, String riskTolerance) {
        String tenantId = TenantContext.require().toString();
        int stockPercentage = 110 - age; // classic rule of thumb

        if ("CONSERVATIVE".equalsIgnoreCase(riskTolerance)) {
            stockPercentage -= 10;
        } else if ("AGGRESSIVE".equalsIgnoreCase(riskTolerance)) {
            stockPercentage += 10;
        }

        stockPercentage = Math.max(0, Math.min(100, stockPercentage));
        int bondPercentage = 100 - stockPercentage;
        int cashPercentage = 0; // Simplified

        if ("CONSERVATIVE".equalsIgnoreCase(riskTolerance)) {
            cashPercentage = 5;
            bondPercentage -= 5;
        }

        AllocationRecommendation rec = new AllocationRecommendation();
        rec.setTenantId(tenantId);
        rec.setAge(age);
        rec.setRiskTolerance(riskTolerance);
        rec.setStocksPercentage(stockPercentage);
        rec.setBondsPercentage(bondPercentage);
        rec.setCashPercentage(cashPercentage);
        rec.setRecommendationText(String.format("Based on age %d and %s risk tolerance, we recommend %d%% stocks, %d%% bonds, and %d%% cash.",
                age, riskTolerance, stockPercentage, bondPercentage, cashPercentage));

        return recommendationRepository.save(rec);
    }
}
