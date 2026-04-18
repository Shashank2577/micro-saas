package com.microsaas.insuranceai.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "policies")
public class Policy {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "policy_number", nullable = false)
    private String policyNumber;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "policy_type", nullable = false)
    private String policyType;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "premium_amount", nullable = false)
    private BigDecimal premiumAmount;

    @Column(name = "ai_risk_score")
    private BigDecimal aiRiskScore;

    @Column(name = "ai_risk_factors")
    private String aiRiskFactors;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Policy() {
    }

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

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(BigDecimal premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public BigDecimal getAiRiskScore() {
        return aiRiskScore;
    }

    public void setAiRiskScore(BigDecimal aiRiskScore) {
        this.aiRiskScore = aiRiskScore;
    }

    public String getAiRiskFactors() {
        return aiRiskFactors;
    }

    public void setAiRiskFactors(String aiRiskFactors) {
        this.aiRiskFactors = aiRiskFactors;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
