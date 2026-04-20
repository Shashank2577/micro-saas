package com.microsaas.budgetpilot.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BudgetItemRequest {
    private String category;
    private String department;
    private BigDecimal allocatedAmount;
}
