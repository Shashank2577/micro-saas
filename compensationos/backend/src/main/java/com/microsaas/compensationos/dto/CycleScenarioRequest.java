package com.microsaas.compensationos.dto;


import java.math.BigDecimal;


public class CycleScenarioRequest {
    private BigDecimal increasePercent;
    private BigDecimal budgetCap;

    public BigDecimal getIncreasePercent() { return this.increasePercent; }
    public void setIncreasePercent(BigDecimal increasePercent) { this.increasePercent = increasePercent; }
    public BigDecimal getBudgetCap() { return this.budgetCap; }
    public void setBudgetCap(BigDecimal budgetCap) { this.budgetCap = budgetCap; }
}
