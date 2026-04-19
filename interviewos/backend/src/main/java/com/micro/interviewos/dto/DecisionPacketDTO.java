package com.micro.interviewos.dto;

import lombok.Data;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Data
public class DecisionPacketDTO {
    private UUID id;
    private UUID tenantId;
    private String name;
    private String status;
    private Map<String, Object> metadataJson;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
