package com.microsaas.competitorradar.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CompetitorFeatureDto {
    private UUID id;
    private UUID competitorId;
    private UUID featureId;
    private String status;
}
