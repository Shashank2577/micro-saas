package com.microsaas.nonprofitos.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "donors")
public class Donor {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String name;

    private String email;

    @Column(name = "total_given")
    private BigDecimal totalGiven;

    @Column(name = "engagement_score")
    private Integer engagementScore;

    @Column(name = "upgrade_potential")
    private String upgradePotential;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    public Donor() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public BigDecimal getTotalGiven() { return totalGiven; }
    public void setTotalGiven(BigDecimal totalGiven) { this.totalGiven = totalGiven; }
    public Integer getEngagementScore() { return engagementScore; }
    public void setEngagementScore(Integer engagementScore) { this.engagementScore = engagementScore; }
    public String getUpgradePotential() { return upgradePotential; }
    public void setUpgradePotential(String upgradePotential) { this.upgradePotential = upgradePotential; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (createdAt == null) createdAt = ZonedDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() { updatedAt = ZonedDateTime.now(); }
}
