package com.microsaas.churnpredictor.dto;




import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;


public class CustomerDto {
    private UUID id;
    private String name;
    private String industry;
    private BigDecimal mrr;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private CustomerHealthScoreDto latestHealthScore;
    private ChurnPredictionDto latestPrediction;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public BigDecimal getMrr() {
        return mrr;
    }

    public void setMrr(BigDecimal mrr) {
        this.mrr = mrr;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public CustomerHealthScoreDto getLatestHealthScore() {
        return latestHealthScore;
    }

    public void setLatestHealthScore(CustomerHealthScoreDto latestHealthScore) {
        this.latestHealthScore = latestHealthScore;
    }

    public ChurnPredictionDto getLatestPrediction() {
        return latestPrediction;
    }

    public void setLatestPrediction(ChurnPredictionDto latestPrediction) {
        this.latestPrediction = latestPrediction;
    }
}
