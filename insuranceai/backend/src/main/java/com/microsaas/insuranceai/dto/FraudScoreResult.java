package com.microsaas.insuranceai.dto;

import java.math.BigDecimal;

public class FraudScoreResult {
    private BigDecimal score;
    private String reasoning;

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }
}
