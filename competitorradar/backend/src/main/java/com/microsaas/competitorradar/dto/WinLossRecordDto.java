package com.microsaas.competitorradar.dto;

import lombok.Data;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class WinLossRecordDto {
    private UUID id;
    private UUID competitorId;
    private String outcome;
    private String reason;
    private BigDecimal value;
    private OffsetDateTime date;
}
