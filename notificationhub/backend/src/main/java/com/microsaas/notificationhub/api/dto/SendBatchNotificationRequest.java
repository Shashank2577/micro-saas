package com.microsaas.notificationhub.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class SendBatchNotificationRequest {
    @NotEmpty
    private List<String> userIds;

    @NotNull
    private UUID templateId;

    @NotBlank
    private String channel;

    private Map<String, String> variables;

    private ZonedDateTime scheduledFor;
}
