package com.microsaas.churnpredictor.entity;

import jakarta.persistence.*;



import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "customer_health_scores")

public class CustomerHealthScore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "composite_score")
    private BigDecimal compositeScore;

    @Column(name = "usage_score")
    private BigDecimal usageScore;

    @Column(name = "support_score")
    private BigDecimal supportScore;

    @Column(name = "engagement_score")
    private BigDecimal engagementScore;

    @Column(name = "nps_score")
    private BigDecimal npsScore;

    @CreationTimestamp
    @Column(name = "calculated_at", updatable = false)
    private OffsetDateTime calculatedAt;

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
