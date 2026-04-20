package com.microsaas.cashflowanalyzer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendations")
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    private String type;
    private String description;

    @Column(name = "potential_savings")
    private BigDecimal potentialSavings;

    @Column(name = "is_implemented")
    private Boolean isImplemented;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Recommendation() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPotentialSavings() { return potentialSavings; }
    public void setPotentialSavings(BigDecimal potentialSavings) { this.potentialSavings = potentialSavings; }
    public Boolean getIsImplemented() { return isImplemented; }
    public void setIsImplemented(Boolean isImplemented) { this.isImplemented = isImplemented; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
