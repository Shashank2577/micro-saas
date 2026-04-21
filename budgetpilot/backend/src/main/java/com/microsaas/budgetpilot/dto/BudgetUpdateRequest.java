package com.microsaas.budgetpilot.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BudgetUpdateRequest {
    private String name;
    private BigDecimal totalAmount;
    private String status;
}
