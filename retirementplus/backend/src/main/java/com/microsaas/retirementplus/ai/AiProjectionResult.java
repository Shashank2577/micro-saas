package com.microsaas.retirementplus.ai;

import java.math.BigDecimal;

public class AiProjectionResult {
    private Integer lifeExpectancy;
    private Integer socialSecurityClaimingAge;
    private BigDecimal estimatedHealthcareCost;
    private Integer qcdOpportunityAge;
    
    private BigDecimal rothConversionAmount;
    private BigDecimal stressTestSurvivalRate;
    private BigDecimal annuityGuaranteedIncome;
    private String taxStrategyOrder;

    public AiProjectionResult() {}

    public Integer getLifeExpectancy() { return lifeExpectancy; }
    public void setLifeExpectancy(Integer lifeExpectancy) { this.lifeExpectancy = lifeExpectancy; }
    public Integer getSocialSecurityClaimingAge() { return socialSecurityClaimingAge; }
    public void setSocialSecurityClaimingAge(Integer socialSecurityClaimingAge) { this.socialSecurityClaimingAge = socialSecurityClaimingAge; }
    public BigDecimal getEstimatedHealthcareCost() { return estimatedHealthcareCost; }
    public void setEstimatedHealthcareCost(BigDecimal estimatedHealthcareCost) { this.estimatedHealthcareCost = estimatedHealthcareCost; }
    public Integer getQcdOpportunityAge() { return qcdOpportunityAge; }
    public void setQcdOpportunityAge(Integer qcdOpportunityAge) { this.qcdOpportunityAge = qcdOpportunityAge; }
    
    public BigDecimal getRothConversionAmount() { return rothConversionAmount; }
    public void setRothConversionAmount(BigDecimal rothConversionAmount) { this.rothConversionAmount = rothConversionAmount; }
    public BigDecimal getStressTestSurvivalRate() { return stressTestSurvivalRate; }
    public void setStressTestSurvivalRate(BigDecimal stressTestSurvivalRate) { this.stressTestSurvivalRate = stressTestSurvivalRate; }
    public BigDecimal getAnnuityGuaranteedIncome() { return annuityGuaranteedIncome; }
    public void setAnnuityGuaranteedIncome(BigDecimal annuityGuaranteedIncome) { this.annuityGuaranteedIncome = annuityGuaranteedIncome; }
    public String getTaxStrategyOrder() { return taxStrategyOrder; }
    public void setTaxStrategyOrder(String taxStrategyOrder) { this.taxStrategyOrder = taxStrategyOrder; }
}
