package com.microsaas.insuranceai.dto;

import java.math.BigDecimal;

public class AnalyticsSummary {
    private long totalClaims;
    private long totalPolicies;
    private BigDecimal averageFraudScore;
    private BigDecimal averageRiskScore;

    public long getTotalClaims() {
        return totalClaims;
    }

    public void setTotalClaims(long totalClaims) {
        this.totalClaims = totalClaims;
    }

    public long getTotalPolicies() {
        return totalPolicies;
    }

    public void setTotalPolicies(long totalPolicies) {
        this.totalPolicies = totalPolicies;
    }

    public BigDecimal getAverageFraudScore() {
        return averageFraudScore;
    }

    public void setAverageFraudScore(BigDecimal averageFraudScore) {
        this.averageFraudScore = averageFraudScore;
    }

    public BigDecimal getAverageRiskScore() {
        return averageRiskScore;
    }

    public void setAverageRiskScore(BigDecimal averageRiskScore) {
        this.averageRiskScore = averageRiskScore;
    }
}
