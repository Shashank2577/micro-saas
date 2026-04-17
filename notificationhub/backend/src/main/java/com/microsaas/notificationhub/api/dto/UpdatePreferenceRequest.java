package com.microsaas.notificationhub.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePreferenceRequest {
    @NotNull
    private Boolean optedIn;
}
