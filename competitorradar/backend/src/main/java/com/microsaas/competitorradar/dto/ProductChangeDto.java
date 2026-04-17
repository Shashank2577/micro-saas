package com.microsaas.competitorradar.dto;

import lombok.Data;
import java.util.UUID;
import java.time.OffsetDateTime;

@Data
public class ProductChangeDto {
    private UUID id;
    private UUID competitorId;
    private String title;
    private String description;
    private String url;
    private OffsetDateTime detectedAt;
    private String status;
}
