package com.microsaas.wealthplan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class GapAnalysisDto {
    private String insuranceType;
    private BigDecimal currentCoverage;
    private BigDecimal recommendedCoverage;
    private BigDecimal gap;
}
