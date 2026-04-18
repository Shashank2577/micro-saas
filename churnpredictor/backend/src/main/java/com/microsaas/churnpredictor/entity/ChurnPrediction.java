package com.microsaas.churnpredictor.entity;

import jakarta.persistence.*;



import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "churn_predictions")

public class ChurnPrediction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "risk_segment")
    private String riskSegment;

    @Column(name = "probability_30_days")
    private BigDecimal probability30Days;

    @Column(name = "probability_60_days")
    private BigDecimal probability60Days;

    @Column(name = "probability_90_days")
    private BigDecimal probability90Days;

    @CreationTimestamp
    @Column(name = "predicted_at", updatable = false)
    private OffsetDateTime predictedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
