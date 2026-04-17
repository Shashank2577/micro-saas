package com.microsaas.notificationhub.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateDto {
    private UUID id;
    private String name;
    private String description;
    private String channel;
    private String subjectTemplate;
    private String contentTemplate;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
