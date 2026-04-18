package com.microsaas.processminer.dto;

import java.time.LocalDateTime;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventIngestRequest(
    @NotBlank String systemType,
    @NotBlank String caseId,
    @NotBlank String activityName,
    String actorId,
    @NotNull LocalDateTime timestamp,
    Map<String, Object> attributes
) {}
