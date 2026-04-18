package com.microsaas.insuranceai.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "claims")
public class Claim {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "claim_number", nullable = false)
    private String claimNumber;

    @Column(name = "policy_number", nullable = false)
    private String policyNumber;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "incident_date", nullable = false)
    private LocalDateTime incidentDate;

    @Column(name = "filed_date", nullable = false)
    private LocalDateTime filedDate;

    @Column(name = "ai_fraud_score")
    private BigDecimal aiFraudScore;

    @Column(name = "ai_fraud_reasoning")
    private String aiFraudReasoning;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Claim() {
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

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(LocalDateTime incidentDate) {
        this.incidentDate = incidentDate;
    }

    public LocalDateTime getFiledDate() {
        return filedDate;
    }

    public void setFiledDate(LocalDateTime filedDate) {
        this.filedDate = filedDate;
    }

    public BigDecimal getAiFraudScore() {
        return aiFraudScore;
    }

    public void setAiFraudScore(BigDecimal aiFraudScore) {
        this.aiFraudScore = aiFraudScore;
    }

    public String getAiFraudReasoning() {
        return aiFraudReasoning;
    }

    public void setAiFraudReasoning(String aiFraudReasoning) {
        this.aiFraudReasoning = aiFraudReasoning;
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
