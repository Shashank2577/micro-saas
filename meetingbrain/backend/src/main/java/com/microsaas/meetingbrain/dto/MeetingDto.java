package com.microsaas.meetingbrain.dto;

import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class MeetingDto {
    private UUID id;
    private String title;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private String externalId;
    private String platform;
    private String status;
    private String summary;
}
