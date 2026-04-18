package com.microsaas.compensationos.dto;


import java.math.BigDecimal;


public class CycleScenarioResponse {
    private BigDecimal projectedTotalCost;
    private BigDecimal remainingBudget;
    private Integer eligibleEmployees;

    public BigDecimal getProjectedTotalCost() { return this.projectedTotalCost; }
    public void setProjectedTotalCost(BigDecimal projectedTotalCost) { this.projectedTotalCost = projectedTotalCost; }
    public BigDecimal getRemainingBudget() { return this.remainingBudget; }
    public void setRemainingBudget(BigDecimal remainingBudget) { this.remainingBudget = remainingBudget; }
    public Integer getEligibleEmployees() { return this.eligibleEmployees; }
    public void setEligibleEmployees(Integer eligibleEmployees) { this.eligibleEmployees = eligibleEmployees; }
}
