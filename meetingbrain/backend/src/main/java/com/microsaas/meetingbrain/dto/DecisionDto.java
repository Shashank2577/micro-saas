package com.microsaas.meetingbrain.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class DecisionDto {
    private UUID id;
    private UUID meetingId;
    private String topic;
    private String description;
    private String decisionText;
}
