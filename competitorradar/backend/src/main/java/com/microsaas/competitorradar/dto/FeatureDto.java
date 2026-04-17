package com.microsaas.competitorradar.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class FeatureDto {
    private UUID id;
    private String name;
    private String category;
}
