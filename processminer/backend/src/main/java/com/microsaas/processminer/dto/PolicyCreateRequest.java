package com.microsaas.processminer.dto;

import java.util.UUID;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PolicyCreateRequest(
    @NotNull UUID processModelId,
    @NotBlank String name,
    @NotNull Map<String, Object> ruleDefinition
) {}
