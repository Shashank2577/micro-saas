package com.microsaas.competitorradar.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;
import java.time.OffsetDateTime;

@Data
public class PricingChangeDto {
    private UUID id;
    private UUID competitorId;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private String planName;
    private OffsetDateTime detectedAt;
}
