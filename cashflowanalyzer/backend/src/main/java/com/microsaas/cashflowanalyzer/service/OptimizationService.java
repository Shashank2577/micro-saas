package com.microsaas.cashflowanalyzer.service;

import com.microsaas.cashflowanalyzer.model.Recommendation;
import com.microsaas.cashflowanalyzer.model.RecurringCharge;
import com.microsaas.cashflowanalyzer.repository.RecommendationRepository;
import com.microsaas.cashflowanalyzer.repository.RecurringChargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OptimizationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private RecurringChargeRepository recurringChargeRepository;

    public List<RecurringCharge> findRecurringCharges(String tenantId) {
        return recurringChargeRepository.findByTenantId(tenantId);
    }

    public List<Recommendation> getOptimizationRecommendations(String tenantId) {
        // Mock generation
        Recommendation rec = new Recommendation();
        rec.setTenantId(tenantId);
        rec.setType("SUBSCRIPTION");
        rec.setDescription("Eliminate $50 unused subscription");
        rec.setPotentialSavings(BigDecimal.valueOf(600.00));
        rec.setIsImplemented(false);
        rec.setCreatedAt(LocalDateTime.now());
        recommendationRepository.save(rec);

        return recommendationRepository.findByTenantId(tenantId);
    }
}
