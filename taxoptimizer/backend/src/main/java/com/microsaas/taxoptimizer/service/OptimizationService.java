package com.microsaas.taxoptimizer.service;

import com.microsaas.taxoptimizer.domain.entity.TaxProfile;
import com.microsaas.taxoptimizer.domain.entity.TaxScenario;
import com.microsaas.taxoptimizer.domain.repository.TaxScenarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OptimizationService {

    private final TaxScenarioRepository taxScenarioRepository;

    public Map<String, Object> optimizeCharitableGiving(BigDecimal income) {
        Map<String, Object> result = new HashMap<>();
        if (income.compareTo(new BigDecimal("150000")) > 0) {
            result.put("strategy", "Donor-Advised Fund");
            result.put("description", "Consider bunching multiple years of donations into a donor-advised fund to exceed standard deduction limits.");
        } else {
            result.put("strategy", "Standard Deduction");
            result.put("description", "Charitable donations may not exceed the standard deduction.");
        }
        return result;
    }

    public TaxScenario modelEntityStructure(UUID tenantId, TaxProfile profile, String targetEntity) {
        Map<String, Object> scenarioData = new HashMap<>();
        scenarioData.put("targetEntity", targetEntity);
        if ("S-Corp".equalsIgnoreCase(targetEntity)) {
            scenarioData.put("taxSavingsEstimate", "15.3% on distributions instead of salary");
            scenarioData.put("requirements", "Reasonable salary, payroll setup");
        } else {
            scenarioData.put("taxSavingsEstimate", "None");
        }

        TaxScenario scenario = TaxScenario.builder()
                .tenantId(tenantId)
                .profile(profile)
                .name("Entity Structure: " + targetEntity)
                .description("Modeling impact of switching to " + targetEntity)
                .scenarioData(scenarioData)
                .build();

        return taxScenarioRepository.save(scenario);
    }
}
