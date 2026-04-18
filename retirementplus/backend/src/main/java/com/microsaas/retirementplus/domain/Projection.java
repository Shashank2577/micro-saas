package com.microsaas.retirementplus.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.math.BigDecimal;

@Entity
@Table(name = "projections")
public class Projection {
    @Id
    private UUID id;
    private UUID tenantId;
    private UUID userId;
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

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    public Projection() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
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

    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }
}
