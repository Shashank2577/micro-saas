package com.microsaas.notificationhub.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Data
public class SendNotificationRequest {
    @NotBlank
    private String userId;

    @NotNull
    private UUID templateId;

    @NotBlank
    private String channel;

    private Map<String, String> variables;

    private ZonedDateTime scheduledFor;
}
