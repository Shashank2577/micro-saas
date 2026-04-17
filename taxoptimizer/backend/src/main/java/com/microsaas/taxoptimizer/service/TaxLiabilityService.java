package com.microsaas.taxoptimizer.service;

import com.microsaas.taxoptimizer.domain.entity.TaxCalculation;
import com.microsaas.taxoptimizer.domain.entity.TaxProfile;
import com.microsaas.taxoptimizer.domain.repository.TaxCalculationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaxLiabilityService {

    private final IncomeExpenseService incomeExpenseService;
    private final TaxCalculationRepository taxCalculationRepository;

    @Transactional
    public TaxCalculation calculateLiability(UUID tenantId, TaxProfile profile, Integer taxYear, String calculationType) {
        Map<String, BigDecimal> incomeSummary = incomeExpenseService.getIncomeSummary(tenantId, profile.getId(), taxYear);
        Map<String, BigDecimal> expenseSummary = incomeExpenseService.getExpenseSummary(tenantId, profile.getId(), taxYear);

        BigDecimal totalIncome = incomeSummary.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpenses = expenseSummary.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        // Simple mock calculations for tax liability based on current year rules assumption
        BigDecimal taxableIncome = totalIncome.subtract(totalExpenses).max(BigDecimal.ZERO);

        // Assuming a flat 22% federal and 5% state tax rate for simplistic projection
        BigDecimal federalLiability = taxableIncome.multiply(new BigDecimal("0.22"));
        BigDecimal stateLiability = taxableIncome.multiply(new BigDecimal("0.05"));

        if ("QUARTERLY".equals(calculationType)) {
            federalLiability = federalLiability.divide(new BigDecimal("4"), 2, java.math.RoundingMode.HALF_UP);
            stateLiability = stateLiability.divide(new BigDecimal("4"), 2, java.math.RoundingMode.HALF_UP);
        }

        BigDecimal totalLiability = federalLiability.add(stateLiability);

        Map<String, Object> details = new HashMap<>();
        details.put("totalIncome", totalIncome);
        details.put("totalExpenses", totalExpenses);
        details.put("taxableIncome", taxableIncome);

        TaxCalculation calculation = TaxCalculation.builder()
                .tenantId(tenantId)
                .profile(profile)
                .taxYear(taxYear)
                .calculationType(calculationType)
                .federalLiability(federalLiability)
                .stateLiability(stateLiability)
                .totalLiability(totalLiability)
                .calculationDetails(details)
                .build();

        return taxCalculationRepository.save(calculation);
    }
}
