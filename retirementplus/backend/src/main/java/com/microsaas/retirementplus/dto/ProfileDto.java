package com.microsaas.retirementplus.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import java.math.BigDecimal;

public class ProfileDto {
    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Current age is required")
    @Min(value = 0, message = "Current age must be at least 0")
    private Integer currentAge;

    @NotNull(message = "Retirement age is required")
    @Min(value = 0, message = "Retirement age must be at least 0")
    private Integer retirementAge;

    @NotNull(message = "Current savings is required")
    @Min(value = 0, message = "Current savings must be at least 0")
    private BigDecimal currentSavings;

    @NotNull(message = "Desired income is required")
    @Min(value = 0, message = "Desired income must be at least 0")
    private BigDecimal desiredIncome;

    private String gender;
    private String healthStatus;
    private String familyHistory;
    private BigDecimal inheritanceGoal;
    private Boolean wantsAnnuity;

    public ProfileDto() {}

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public Integer getCurrentAge() { return currentAge; }
    public void setCurrentAge(Integer currentAge) { this.currentAge = currentAge; }
    public Integer getRetirementAge() { return retirementAge; }
    public void setRetirementAge(Integer retirementAge) { this.retirementAge = retirementAge; }
    public BigDecimal getCurrentSavings() { return currentSavings; }
    public void setCurrentSavings(BigDecimal currentSavings) { this.currentSavings = currentSavings; }
    public BigDecimal getDesiredIncome() { return desiredIncome; }
    public void setDesiredIncome(BigDecimal desiredIncome) { this.desiredIncome = desiredIncome; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
    public String getFamilyHistory() { return familyHistory; }
    public void setFamilyHistory(String familyHistory) { this.familyHistory = familyHistory; }
    public BigDecimal getInheritanceGoal() { return inheritanceGoal; }
    public void setInheritanceGoal(BigDecimal inheritanceGoal) { this.inheritanceGoal = inheritanceGoal; }
    public Boolean getWantsAnnuity() { return wantsAnnuity; }
    public void setWantsAnnuity(Boolean wantsAnnuity) { this.wantsAnnuity = wantsAnnuity; }
}
