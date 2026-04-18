package com.microsaas.compensationos.entity;

import jakarta.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "compensation_cycles")

 
 
 
public class CompensationCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "cycle_name", nullable = false)
    private String cycleName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "total_budget", precision = 15, scale = 2)
    private BigDecimal totalBudget;

     
    private String status = "DRAFT"; // DRAFT, ACTIVE, COMPLETED

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    public UUID getId() { return this.id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return this.tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getCycleName() { return this.cycleName; }
    public void setCycleName(String cycleName) { this.cycleName = cycleName; }
    public LocalDate getStartDate() { return this.startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return this.endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public BigDecimal getTotalBudget() { return this.totalBudget; }
    public void setTotalBudget(BigDecimal totalBudget) { this.totalBudget = totalBudget; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public OffsetDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return this.updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
