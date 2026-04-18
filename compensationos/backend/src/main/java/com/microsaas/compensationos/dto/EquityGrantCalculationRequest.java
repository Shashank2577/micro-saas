package com.microsaas.compensationos.dto;


import java.util.UUID;


public class EquityGrantCalculationRequest {
    private UUID planId;
    private Long shares;
    private String vestingStartDate;

    public UUID getPlanId() { return this.planId; }
    public void setPlanId(UUID planId) { this.planId = planId; }
    public Long getShares() { return this.shares; }
    public void setShares(Long shares) { this.shares = shares; }
    public String getVestingStartDate() { return this.vestingStartDate; }
    public void setVestingStartDate(String vestingStartDate) { this.vestingStartDate = vestingStartDate; }
}
