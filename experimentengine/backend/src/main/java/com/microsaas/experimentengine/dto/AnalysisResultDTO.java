package com.microsaas.experimentengine.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResultDTO {
    private Double pValue;
    private Double confidenceIntervalLower;
    private Double confidenceIntervalUpper;
    private Double liftPercentage;
}
