package com.microsaas.competitorradar.dto;

import lombok.Data;
import java.util.UUID;
import java.time.OffsetDateTime;

@Data
public class BattlecardDto {
    private UUID id;
    private UUID competitorId;
    private String content;
    private OffsetDateTime generatedAt;
}
