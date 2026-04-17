package com.microsaas.notificationhub.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreferenceDto {
    private UUID id;
    private String userId;
    private String channel;
    private Boolean optedIn;
}
