package com.microsaas.retirementplus.dto;

import java.math.BigDecimal;

public class ProjectionResponseDto {
    private Integer lifeExpectancy;
    private BigDecimal safeWithdrawalRate;
    private Integer socialSecurityClaimingAge;
    private BigDecimal estimatedHealthcareCost;
    private Integer qcdOpportunityAge;
    private BigDecimal probabilityOfSuccess;
    
    private BigDecimal rothConversionAmount;
    private BigDecimal rmdAmount;
    private BigDecimal stressTestSurvivalRate;
    private BigDecimal annuityGuaranteedIncome;
    private String taxStrategyOrder;

    public ProjectionResponseDto() {}

    public Integer getLifeExpectancy() { return lifeExpectancy; }
    public void setLifeExpectancy(Integer lifeExpectancy) { this.lifeExpectancy = lifeExpectancy; }
    public BigDecimal getSafeWithdrawalRate() { return safeWithdrawalRate; }
    public void setSafeWithdrawalRate(BigDecimal safeWithdrawalRate) { this.safeWithdrawalRate = safeWithdrawalRate; }
    public Integer getSocialSecurityClaimingAge() { return socialSecurityClaimingAge; }
    public void setSocialSecurityClaimingAge(Integer socialSecurityClaimingAge) { this.socialSecurityClaimingAge = socialSecurityClaimingAge; }
    public BigDecimal getEstimatedHealthcareCost() { return estimatedHealthcareCost; }
    public void setEstimatedHealthcareCost(BigDecimal estimatedHealthcareCost) { this.estimatedHealthcareCost = estimatedHealthcareCost; }
    public Integer getQcdOpportunityAge() { return qcdOpportunityAge; }
    public void setQcdOpportunityAge(Integer qcdOpportunityAge) { this.qcdOpportunityAge = qcdOpportunityAge; }
    public BigDecimal getProbabilityOfSuccess() { return probabilityOfSuccess; }
    public void setProbabilityOfSuccess(BigDecimal probabilityOfSuccess) { this.probabilityOfSuccess = probabilityOfSuccess; }

    public BigDecimal getRothConversionAmount() { return rothConversionAmount; }
    public void setRothConversionAmount(BigDecimal rothConversionAmount) { this.rothConversionAmount = rothConversionAmount; }
    public BigDecimal getRmdAmount() { return rmdAmount; }
    public void setRmdAmount(BigDecimal rmdAmount) { this.rmdAmount = rmdAmount; }
    public BigDecimal getStressTestSurvivalRate() { return stressTestSurvivalRate; }
    public void setStressTestSurvivalRate(BigDecimal stressTestSurvivalRate) { this.stressTestSurvivalRate = stressTestSurvivalRate; }
    public BigDecimal getAnnuityGuaranteedIncome() { return annuityGuaranteedIncome; }
    public void setAnnuityGuaranteedIncome(BigDecimal annuityGuaranteedIncome) { this.annuityGuaranteedIncome = annuityGuaranteedIncome; }
    public String getTaxStrategyOrder() { return taxStrategyOrder; }
    public void setTaxStrategyOrder(String taxStrategyOrder) { this.taxStrategyOrder = taxStrategyOrder; }
}
