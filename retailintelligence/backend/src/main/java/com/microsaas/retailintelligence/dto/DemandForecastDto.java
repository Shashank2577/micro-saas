package com.microsaas.retailintelligence.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class DemandForecastDto {
    private UUID id;
    private UUID skuId;
    private LocalDate forecastDate;
    private int predictedDemand;
    private BigDecimal confidenceScore;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getSkuId() { return skuId; }
    public void setSkuId(UUID skuId) { this.skuId = skuId; }
    public LocalDate getForecastDate() { return forecastDate; }
    public void setForecastDate(LocalDate forecastDate) { this.forecastDate = forecastDate; }
    public int getPredictedDemand() { return predictedDemand; }
    public void setPredictedDemand(int predictedDemand) { this.predictedDemand = predictedDemand; }
    public BigDecimal getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }
}
