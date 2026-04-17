package com.microsaas.competitorradar.dto;

import lombok.Data;
import java.util.UUID;
import java.time.OffsetDateTime;

@Data
public class CompetitorDto {
    private UUID id;
    private String name;
    private String website;
    private String industry;
    private String description;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
