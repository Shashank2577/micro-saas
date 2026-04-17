package com.microsaas.competitorradar.dto;

import lombok.Data;
import java.util.UUID;
import java.time.OffsetDateTime;

@Data
public class HiringSignalDto {
    private UUID id;
    private UUID competitorId;
    private String roleTitle;
    private String department;
    private String location;
    private String source;
    private OffsetDateTime postedAt;
}
