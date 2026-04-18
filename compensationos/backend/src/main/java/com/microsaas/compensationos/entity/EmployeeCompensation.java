package com.microsaas.compensationos.entity;

import jakarta.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "employee_compensation")

 
 
 
public class EmployeeCompensation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String level;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String location;

    @Column(name = "base_salary", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseSalary;

    @Column(name = "bonus_target_percent", precision = 5, scale = 2)
    private BigDecimal bonusTargetPercent;

    @Column(name = "equity_grant_value", precision = 10, scale = 2)
    private BigDecimal equityGrantValue;

    @Column(length = 3)
     
    private String currency = "USD";

    private String gender;
    private String ethnicity;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    public UUID getId() { return this.id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return this.tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public UUID getEmployeeId() { return this.employeeId; }
    public void setEmployeeId(UUID employeeId) { this.employeeId = employeeId; }
    public String getFirstName() { return this.firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return this.lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getRole() { return this.role; }
    public void setRole(String role) { this.role = role; }
    public String getLevel() { return this.level; }
    public void setLevel(String level) { this.level = level; }
    public String getDepartment() { return this.department; }
    public void setDepartment(String department) { this.department = department; }
    public String getLocation() { return this.location; }
    public void setLocation(String location) { this.location = location; }
    public BigDecimal getBaseSalary() { return this.baseSalary; }
    public void setBaseSalary(BigDecimal baseSalary) { this.baseSalary = baseSalary; }
    public BigDecimal getBonusTargetPercent() { return this.bonusTargetPercent; }
    public void setBonusTargetPercent(BigDecimal bonusTargetPercent) { this.bonusTargetPercent = bonusTargetPercent; }
    public BigDecimal getEquityGrantValue() { return this.equityGrantValue; }
    public void setEquityGrantValue(BigDecimal equityGrantValue) { this.equityGrantValue = equityGrantValue; }
    public String getCurrency() { return this.currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getGender() { return this.gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getEthnicity() { return this.ethnicity; }
    public void setEthnicity(String ethnicity) { this.ethnicity = ethnicity; }
    public LocalDate getHireDate() { return this.hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    public OffsetDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return this.updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
