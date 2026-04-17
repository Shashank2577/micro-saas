package com.microsaas.wealthplan.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ScenarioDto {
    private UUID goalId;
    private String name;
    private BigDecimal assumedReturnRate;
    private BigDecimal inflationRate;
    private BigDecimal monthlyContribution;
}
