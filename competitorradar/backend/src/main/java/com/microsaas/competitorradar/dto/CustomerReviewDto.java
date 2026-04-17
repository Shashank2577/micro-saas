package com.microsaas.competitorradar.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;
import java.time.OffsetDateTime;

@Data
public class CustomerReviewDto {
    private UUID id;
    private UUID competitorId;
    private String platform;
    private Integer rating;
    private String text;
    private String category;
    private BigDecimal sentimentScore;
    private OffsetDateTime postedAt;
}
