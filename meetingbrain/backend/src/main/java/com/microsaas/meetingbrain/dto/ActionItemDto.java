package com.microsaas.meetingbrain.dto;

import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class ActionItemDto {
    private UUID id;
    private UUID meetingId;
    private String description;
    private String owner;
    private OffsetDateTime dueDate;
    private String status;
}
