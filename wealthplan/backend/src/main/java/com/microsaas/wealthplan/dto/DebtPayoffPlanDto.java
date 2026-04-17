package com.microsaas.wealthplan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class DebtPayoffPlanDto {
    private String recommendedStrategy;
    private List<UUID> payoffOrder;
    private int monthsToFreedom;
}
