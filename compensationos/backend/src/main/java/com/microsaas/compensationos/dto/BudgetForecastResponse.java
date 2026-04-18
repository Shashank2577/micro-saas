package com.microsaas.compensationos.dto;

import java.math.BigDecimal;
import java.util.Map;

public class BudgetForecastResponse {
    private Map<String, BigDecimal> monthlyProjections;
    private BigDecimal totalAnnualForecast;

    public Map<String, BigDecimal> getMonthlyProjections() { return this.monthlyProjections; }
    public void setMonthlyProjections(Map<String, BigDecimal> monthlyProjections) { this.monthlyProjections = monthlyProjections; }
    public BigDecimal getTotalAnnualForecast() { return this.totalAnnualForecast; }
    public void setTotalAnnualForecast(BigDecimal totalAnnualForecast) { this.totalAnnualForecast = totalAnnualForecast; }
}
