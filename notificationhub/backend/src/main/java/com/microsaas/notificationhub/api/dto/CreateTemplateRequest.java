package com.microsaas.notificationhub.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTemplateRequest {
    @NotBlank
    private String name;
    private String description;
    @NotBlank
    private String channel;
    private String subjectTemplate;
    @NotBlank
    private String contentTemplate;
}
