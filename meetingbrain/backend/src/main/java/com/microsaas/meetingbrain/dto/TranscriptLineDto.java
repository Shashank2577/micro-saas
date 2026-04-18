package com.microsaas.meetingbrain.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class TranscriptLineDto {
    private UUID id;
    private String speaker;
    private String text;
    private Double startTimestamp;
    private Double endTimestamp;
}
