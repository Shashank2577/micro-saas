package com.microsaas.budgetpilot.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BudgetCreateRequest {
    private String name;
    private Integer fiscalYear;
    private BigDecimal totalAmount;
}
