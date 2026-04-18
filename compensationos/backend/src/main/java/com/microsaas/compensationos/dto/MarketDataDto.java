package com.microsaas.compensationos.dto;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


public class MarketDataDto {
    private UUID id;
    private String role;
    private String level;
    private String location;
    private BigDecimal p25Salary;
    private BigDecimal p50Salary;
    private BigDecimal p75Salary;
    private BigDecimal p90Salary;
    private String dataSource;
    private LocalDate effectiveDate;

    public UUID getId() { return this.id; }
    public void setId(UUID id) { this.id = id; }
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
}
