package com.microsaas.competitorradar.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;
import java.time.OffsetDateTime;

@Data
public class SocialMentionDto {
    private UUID id;
    private UUID competitorId;
    private String platform;
    private String text;
    private String url;
    private BigDecimal sentimentScore;
    private OffsetDateTime postedAt;
}
