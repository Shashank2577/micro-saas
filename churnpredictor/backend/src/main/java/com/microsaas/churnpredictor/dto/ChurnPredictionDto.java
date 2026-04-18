package com.microsaas.churnpredictor.dto;




import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;


public class ChurnPredictionDto {
    private UUID id;
    private UUID customerId;
    private String riskSegment;
    private BigDecimal probability30Days;
    private BigDecimal probability60Days;
    private BigDecimal probability90Days;
    private OffsetDateTime predictedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getRiskSegment() {
        return riskSegment;
    }

    public void setRiskSegment(String riskSegment) {
        this.riskSegment = riskSegment;
    }

    public BigDecimal getProbability30Days() {
        return probability30Days;
    }

    public void setProbability30Days(BigDecimal probability30Days) {
        this.probability30Days = probability30Days;
    }

    public BigDecimal getProbability60Days() {
        return probability60Days;
    }

    public void setProbability60Days(BigDecimal probability60Days) {
        this.probability60Days = probability60Days;
    }

    public BigDecimal getProbability90Days() {
        return probability90Days;
    }

    public void setProbability90Days(BigDecimal probability90Days) {
        this.probability90Days = probability90Days;
    }

    public OffsetDateTime getPredictedAt() {
        return predictedAt;
    }

    public void setPredictedAt(OffsetDateTime predictedAt) {
        this.predictedAt = predictedAt;
    }
}
