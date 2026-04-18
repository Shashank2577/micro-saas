package com.microsaas.restaurantintel.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class PredictiveOrder {
    @Id
    private UUID id;
    private UUID tenantId;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private BigDecimal predictedDemand;
    private BigDecimal recommendedOrderAmount;
    private LocalDate orderDate;
    private String status;
    private BigDecimal aiConfidenceScore;
    private String aiRationale;
    private Instant createdAt;
    private Instant updatedAt;

    public PredictiveOrder() {}

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public Ingredient getIngredient() { return ingredient; }
    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }
    public BigDecimal getPredictedDemand() { return predictedDemand; }
    public void setPredictedDemand(BigDecimal predictedDemand) { this.predictedDemand = predictedDemand; }
    public BigDecimal getRecommendedOrderAmount() { return recommendedOrderAmount; }
    public void setRecommendedOrderAmount(BigDecimal recommendedOrderAmount) { this.recommendedOrderAmount = recommendedOrderAmount; }
    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getAiConfidenceScore() { return aiConfidenceScore; }
    public void setAiConfidenceScore(BigDecimal aiConfidenceScore) { this.aiConfidenceScore = aiConfidenceScore; }
    public String getAiRationale() { return aiRationale; }
    public void setAiRationale(String aiRationale) { this.aiRationale = aiRationale; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
