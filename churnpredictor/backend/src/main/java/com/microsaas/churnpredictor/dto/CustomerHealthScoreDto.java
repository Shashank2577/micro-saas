package com.microsaas.churnpredictor.dto;




import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;


public class CustomerHealthScoreDto {
    private UUID id;
    private UUID customerId;
    private BigDecimal compositeScore;
    private BigDecimal usageScore;
    private BigDecimal supportScore;
    private BigDecimal engagementScore;
    private BigDecimal npsScore;
    private OffsetDateTime calculatedAt;

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

    public BigDecimal getCompositeScore() {
        return compositeScore;
    }

    public void setCompositeScore(BigDecimal compositeScore) {
        this.compositeScore = compositeScore;
    }

    public BigDecimal getUsageScore() {
        return usageScore;
    }

    public void setUsageScore(BigDecimal usageScore) {
        this.usageScore = usageScore;
    }

    public BigDecimal getSupportScore() {
        return supportScore;
    }

    public void setSupportScore(BigDecimal supportScore) {
        this.supportScore = supportScore;
    }

    public BigDecimal getEngagementScore() {
        return engagementScore;
    }

    public void setEngagementScore(BigDecimal engagementScore) {
        this.engagementScore = engagementScore;
    }

    public BigDecimal getNpsScore() {
        return npsScore;
    }

    public void setNpsScore(BigDecimal npsScore) {
        this.npsScore = npsScore;
    }

    public OffsetDateTime getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(OffsetDateTime calculatedAt) {
        this.calculatedAt = calculatedAt;
    }
}
