package com.microsaas.competitorradar.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;
import java.time.OffsetDateTime;

@Data
public class PressMentionDto {
    private UUID id;
    private UUID competitorId;
    private String source;
    private String title;
    private String url;
    private BigDecimal sentimentScore;
    private OffsetDateTime publishedAt;
}
