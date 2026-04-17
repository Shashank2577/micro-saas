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
public class NotificationDto {
    private UUID id;
    private String userId;
    private UUID templateId;
    private String channel;
    private String subject;
    private String content;
    private String status;
    private ZonedDateTime scheduledFor;
    private ZonedDateTime createdAt;
}
