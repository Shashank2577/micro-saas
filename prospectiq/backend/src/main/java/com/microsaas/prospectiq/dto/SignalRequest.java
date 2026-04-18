package com.microsaas.prospectiq.dto;

import lombok.Data;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class SignalRequest {
    private UUID prospectId;
    private String type;
    private String source;
    private String content;
    private ZonedDateTime detectedAt;
}
