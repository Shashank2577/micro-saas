package com.microsaas.wealthplan.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.wealthplan.dto.GapAnalysisDto;
import com.microsaas.wealthplan.entity.InsurancePolicy;
import com.microsaas.wealthplan.repository.InsurancePolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InsuranceService {
    private final InsurancePolicyRepository insuranceRepository;

    public List<GapAnalysisDto> analyzeGaps() {
        String tenantId = TenantContext.require().toString();
        List<InsurancePolicy> policies = insuranceRepository.findByTenantId(tenantId);

        BigDecimal totalLife = BigDecimal.ZERO;
        BigDecimal totalDisability = BigDecimal.ZERO;

        for (InsurancePolicy p : policies) {
            if ("LIFE".equalsIgnoreCase(p.getType())) {
                totalLife = totalLife.add(p.getCoverageAmount());
            } else if ("DISABILITY".equalsIgnoreCase(p.getType())) {
                totalDisability = totalDisability.add(p.getCoverageAmount());
            }
        }

        // Simple rules for gaps
        BigDecimal recommendedLife = new BigDecimal("1000000"); // Standard rule of thumb
        BigDecimal recommendedDisability = new BigDecimal("5000"); // Monthly benefit

        List<GapAnalysisDto> gaps = new ArrayList<>();

        BigDecimal lifeGap = recommendedLife.subtract(totalLife);
        if (lifeGap.compareTo(BigDecimal.ZERO) < 0) lifeGap = BigDecimal.ZERO;
        gaps.add(new GapAnalysisDto("LIFE", totalLife, recommendedLife, lifeGap));

        BigDecimal disGap = recommendedDisability.subtract(totalDisability);
        if (disGap.compareTo(BigDecimal.ZERO) < 0) disGap = BigDecimal.ZERO;
        gaps.add(new GapAnalysisDto("DISABILITY", totalDisability, recommendedDisability, disGap));

        return gaps;
    }
}
