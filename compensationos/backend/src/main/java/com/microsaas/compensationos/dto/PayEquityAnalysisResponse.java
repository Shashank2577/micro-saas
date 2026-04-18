package com.microsaas.compensationos.dto;

import java.math.BigDecimal;
import java.util.Map;

public class PayEquityAnalysisResponse {
    private String role;
    private Map<String, BigDecimal> avgSalaryByGender;
    private Map<String, BigDecimal> avgSalaryByEthnicity;
    private String aiInsight;

    public String getRole() { return this.role; }
    public void setRole(String role) { this.role = role; }
    public Map<String, BigDecimal> getAvgSalaryByGender() { return this.avgSalaryByGender; }
    public void setAvgSalaryByGender(Map<String, BigDecimal> avgSalaryByGender) { this.avgSalaryByGender = avgSalaryByGender; }
    public Map<String, BigDecimal> getAvgSalaryByEthnicity() { return this.avgSalaryByEthnicity; }
    public void setAvgSalaryByEthnicity(Map<String, BigDecimal> avgSalaryByEthnicity) { this.avgSalaryByEthnicity = avgSalaryByEthnicity; }
    public String getAiInsight() { return this.aiInsight; }
    public void setAiInsight(String aiInsight) { this.aiInsight = aiInsight; }
}
