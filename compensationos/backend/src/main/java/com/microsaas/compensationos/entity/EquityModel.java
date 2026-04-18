package com.microsaas.compensationos.entity;

import jakarta.persistence.*;


import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "equity_models")

 
 
 
public class EquityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "plan_name", nullable = false)
    private String planName;

    @Column(name = "total_shares")
    private Long totalShares;

    @Column(name = "available_shares")
    private Long availableShares;

    @Column(name = "current_valuation", precision = 10, scale = 2)
    private BigDecimal currentValuation;

    @Column(name = "vesting_schedule_months")
     
    private Integer vestingScheduleMonths = 48;

    @Column(name = "cliff_months")
     
    private Integer cliffMonths = 12;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    public UUID getId() { return this.id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return this.tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getPlanName() { return this.planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public Long getTotalShares() { return this.totalShares; }
    public void setTotalShares(Long totalShares) { this.totalShares = totalShares; }
    public Long getAvailableShares() { return this.availableShares; }
    public void setAvailableShares(Long availableShares) { this.availableShares = availableShares; }
    public BigDecimal getCurrentValuation() { return this.currentValuation; }
    public void setCurrentValuation(BigDecimal currentValuation) { this.currentValuation = currentValuation; }
    public Integer getVestingScheduleMonths() { return this.vestingScheduleMonths; }
    public void setVestingScheduleMonths(Integer vestingScheduleMonths) { this.vestingScheduleMonths = vestingScheduleMonths; }
    public Integer getCliffMonths() { return this.cliffMonths; }
    public void setCliffMonths(Integer cliffMonths) { this.cliffMonths = cliffMonths; }
    public OffsetDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return this.updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
