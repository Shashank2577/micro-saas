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
public class ForgivenessEligibilityDto {
    private UUID debtId;
    private String debtName;
    private boolean isEligible;
    private String programName;
    private String requirements;
}
