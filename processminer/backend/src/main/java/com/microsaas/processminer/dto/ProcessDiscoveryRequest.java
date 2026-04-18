package com.microsaas.processminer.dto;

import jakarta.validation.constraints.NotBlank;

public record ProcessDiscoveryRequest(
    @NotBlank String systemType
) {}
