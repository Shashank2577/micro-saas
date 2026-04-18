package com.microsaas.retirementplus.dto;

import java.util.UUID;
import java.math.BigDecimal;

public class ProfileDto {
    private UUID userId;
    private Integer currentAge;
    private Integer retirementAge;
    private BigDecimal currentSavings;
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
