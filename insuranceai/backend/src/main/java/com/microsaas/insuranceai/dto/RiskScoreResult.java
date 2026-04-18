package com.microsaas.insuranceai.dto;

import java.math.BigDecimal;

public class RiskScoreResult {
    private BigDecimal score;
    private String factors;

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getFactors() {
        return factors;
    }

    public void setFactors(String factors) {
        this.factors = factors;
    }
}
