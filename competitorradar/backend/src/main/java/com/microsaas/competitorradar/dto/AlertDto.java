package com.microsaas.competitorradar.dto;

import lombok.Data;
import java.util.UUID;
import java.time.OffsetDateTime;

@Data
public class AlertDto {
    private UUID id;
    private String type;
    private String severity;
    private String message;
    private UUID competitorId;
    private OffsetDateTime createdAt;
}
