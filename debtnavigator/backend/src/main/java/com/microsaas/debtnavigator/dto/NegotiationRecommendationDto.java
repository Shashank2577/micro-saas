package com.microsaas.debtnavigator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NegotiationRecommendationDto {
    private UUID debtId;
    private String debtName;
    private String recommendedScript;
    private int likelihoodOfSuccessPercentage;
}
