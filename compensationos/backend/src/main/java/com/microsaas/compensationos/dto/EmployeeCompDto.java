package com.microsaas.compensationos.dto;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


public class EmployeeCompDto {
    private UUID id;
    private UUID employeeId;
    private String firstName;
    private String lastName;
    private String role;
    private String level;
    private String department;
    private String location;
    private BigDecimal baseSalary;
    private BigDecimal bonusTargetPercent;
    private BigDecimal equityGrantValue;
    private String currency;
    private String gender;
    private String ethnicity;
    private LocalDate hireDate;

    public UUID getId() { return this.id; }
    public void setId(UUID id) { this.id = id; }
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
}
