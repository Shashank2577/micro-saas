package com.microsaas.debtnavigator.service;

import com.microsaas.debtnavigator.dto.ConsolidationEvaluationDto;
import com.microsaas.debtnavigator.entity.ConsolidationLoan;
import com.microsaas.debtnavigator.entity.Debt;
import com.microsaas.debtnavigator.repository.ConsolidationLoanRepository;
import com.microsaas.debtnavigator.repository.DebtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsolidationEvaluationService {

    private final DebtRepository debtRepository;
    private final ConsolidationLoanRepository consolidationLoanRepository;

    public List<ConsolidationEvaluationDto> evaluateConsolidation(String tenantId) {
        List<Debt> debts = debtRepository.findByTenantId(tenantId);
        List<ConsolidationLoan> loans = consolidationLoanRepository.findByTenantId(tenantId);

        BigDecimal totalDebt = debts.stream().map(Debt::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalDebt.compareTo(BigDecimal.ZERO) == 0 || loans.isEmpty()) {
            return new ArrayList<>();
        }

        BigDecimal totalWeightedApr = debts.stream()
                .map(d -> d.getApr().multiply(d.getBalance()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal blendedApr = totalWeightedApr.divide(totalDebt, 4, RoundingMode.HALF_UP);

        List<ConsolidationEvaluationDto> evaluations = new ArrayList<>();

        int limit = Math.min(loans.size(), 5); // Max 5 scenarios constraint
        for (int i = 0; i < limit; i++) {
            ConsolidationLoan loan = loans.get(i);

            if (totalDebt.compareTo(loan.getMaxAmount()) <= 0) {
                boolean recommended = loan.getApr().compareTo(blendedApr) < 0;

                // Simplified savings calculation assuming 36 month term for comparison
                BigDecimal monthlyInterestCurrent = totalDebt.multiply(blendedApr.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP));
                BigDecimal monthlyInterestNew = totalDebt.multiply(loan.getApr().divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP));

                BigDecimal monthlySavings = monthlyInterestCurrent.subtract(monthlyInterestNew);
                BigDecimal totalSavings = monthlySavings.multiply(BigDecimal.valueOf(36)).subtract(loan.getOriginationFee());

                if (totalSavings.compareTo(BigDecimal.ZERO) < 0) {
                    recommended = false;
                }

                evaluations.add(ConsolidationEvaluationDto.builder()
                        .providerName(loan.getProviderName())
                        .requiredLoanAmount(totalDebt)
                        .blendedCurrentApr(blendedApr)
                        .consolidationApr(loan.getApr())
                        .monthlySavings(monthlySavings)
                        .totalInterestSavings(totalSavings)
                        .isRecommended(recommended)
                        .build());
            }
        }

        return evaluations;
    }
}
