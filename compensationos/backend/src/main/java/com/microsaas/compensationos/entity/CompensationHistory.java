package com.microsaas.compensationos.entity;

import jakarta.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "compensation_history")

 
 
 
public class CompensationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    @Column(name = "change_date", nullable = false)
    private LocalDate changeDate;

    @Column(name = "previous_base", precision = 10, scale = 2)
    private BigDecimal previousBase;

    @Column(name = "new_base", precision = 10, scale = 2)
    private BigDecimal newBase;

    private String reason;

    @Column(name = "approved_by")
    private UUID approvedBy;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    public UUID getId() { return this.id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return this.tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public UUID getEmployeeId() { return this.employeeId; }
    public void setEmployeeId(UUID employeeId) { this.employeeId = employeeId; }
    public LocalDate getChangeDate() { return this.changeDate; }
    public void setChangeDate(LocalDate changeDate) { this.changeDate = changeDate; }
    public BigDecimal getPreviousBase() { return this.previousBase; }
    public void setPreviousBase(BigDecimal previousBase) { this.previousBase = previousBase; }
    public BigDecimal getNewBase() { return this.newBase; }
    public void setNewBase(BigDecimal newBase) { this.newBase = newBase; }
    public String getReason() { return this.reason; }
    public void setReason(String reason) { this.reason = reason; }
    public UUID getApprovedBy() { return this.approvedBy; }
    public void setApprovedBy(UUID approvedBy) { this.approvedBy = approvedBy; }
    public OffsetDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
