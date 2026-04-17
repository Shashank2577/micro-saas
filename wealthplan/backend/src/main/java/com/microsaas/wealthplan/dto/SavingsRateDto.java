package com.microsaas.wealthplan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SavingsRateDto {
    private BigDecimal requiredMonthlySavings;
}
