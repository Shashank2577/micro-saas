package com.microsaas.investtracker.dto;

import lombok.Data;

import java.util.List;

@Data
public class AiInsightDto {
    private Integer riskScore;
    private String volatilityAssessment;
    private List<String> rebalanceRecommendations;
}
