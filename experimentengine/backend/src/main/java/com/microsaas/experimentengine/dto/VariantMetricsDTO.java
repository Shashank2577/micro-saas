package com.microsaas.experimentengine.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariantMetricsDTO {
    private UUID variantId;
    private int exposures;
    private int conversions;
    private Double mean;
    private Double stdErr;
}
