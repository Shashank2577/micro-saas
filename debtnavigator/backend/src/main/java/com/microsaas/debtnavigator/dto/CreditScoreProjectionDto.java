package com.microsaas.debtnavigator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditScoreProjectionDto {
    private int currentScore;
    private int projectedScore;
    private int monthsOut;
    private String rationale;
}
