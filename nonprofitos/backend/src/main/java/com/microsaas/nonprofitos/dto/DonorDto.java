package com.microsaas.nonprofitos.dto;

import java.math.BigDecimal;

public class DonorDto {
    private String name;
    private String email;
    private BigDecimal totalGiven;
    private Integer engagementScore;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public BigDecimal getTotalGiven() { return totalGiven; }
    public void setTotalGiven(BigDecimal totalGiven) { this.totalGiven = totalGiven; }
    public Integer getEngagementScore() { return engagementScore; }
    public void setEngagementScore(Integer engagementScore) { this.engagementScore = engagementScore; }
}
