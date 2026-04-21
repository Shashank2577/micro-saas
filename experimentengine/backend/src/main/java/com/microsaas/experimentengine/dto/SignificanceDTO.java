package com.microsaas.experimentengine.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignificanceDTO {
    private boolean isSignificant;
    private Double pValue;
}
