package com.microsaas.compensationos.entity;

import jakarta.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "market_data")

 
 
 
public class MarketData {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String level;

    @Column(nullable = false)
    private String location;

    @Column(name = "p25_salary", precision = 10, scale = 2)
    private BigDecimal p25Salary;

    @Column(name = "p50_salary", precision = 10, scale = 2)
    private BigDecimal p50Salary;

    @Column(name = "p75_salary", precision = 10, scale = 2)
    private BigDecimal p75Salary;

    @Column(name = "p90_salary", precision = 10, scale = 2)
    private BigDecimal p90Salary;

    @Column(name = "data_source")
    private String dataSource;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    public UUID getId() { return this.id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return this.tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getRole() { return this.role; }
    public void setRole(String role) { this.role = role; }
    public String getLevel() { return this.level; }
    public void setLevel(String level) { this.level = level; }
    public String getLocation() { return this.location; }
    public void setLocation(String location) { this.location = location; }
    public BigDecimal getP25Salary() { return this.p25Salary; }
    public void setP25Salary(BigDecimal p25Salary) { this.p25Salary = p25Salary; }
    public BigDecimal getP50Salary() { return this.p50Salary; }
    public void setP50Salary(BigDecimal p50Salary) { this.p50Salary = p50Salary; }
    public BigDecimal getP75Salary() { return this.p75Salary; }
    public void setP75Salary(BigDecimal p75Salary) { this.p75Salary = p75Salary; }
    public BigDecimal getP90Salary() { return this.p90Salary; }
    public void setP90Salary(BigDecimal p90Salary) { this.p90Salary = p90Salary; }
    public String getDataSource() { return this.dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }
    public LocalDate getEffectiveDate() { return this.effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    public OffsetDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return this.updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
