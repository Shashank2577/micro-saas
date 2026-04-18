package com.microsaas.retailintelligence.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class PricingRecommendationDto {
    private UUID id;
    private UUID skuId;
    private BigDecimal recommendedPrice;
    private String reasoning;
    private BigDecimal marginPercentage;
    private String status;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getSkuId() { return skuId; }
    public void setSkuId(UUID skuId) { this.skuId = skuId; }
    public BigDecimal getRecommendedPrice() { return recommendedPrice; }
    public void setRecommendedPrice(BigDecimal recommendedPrice) { this.recommendedPrice = recommendedPrice; }
    public String getReasoning() { return reasoning; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }
    public BigDecimal getMarginPercentage() { return marginPercentage; }
    public void setMarginPercentage(BigDecimal marginPercentage) { this.marginPercentage = marginPercentage; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
