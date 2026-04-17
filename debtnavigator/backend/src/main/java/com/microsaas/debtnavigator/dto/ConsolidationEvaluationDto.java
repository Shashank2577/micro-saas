package com.microsaas.debtnavigator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsolidationEvaluationDto {
    private String providerName;
    private BigDecimal requiredLoanAmount;
    private BigDecimal blendedCurrentApr;
    private BigDecimal consolidationApr;
    private BigDecimal monthlySavings;
    private BigDecimal totalInterestSavings;
    private boolean isRecommended;
}
