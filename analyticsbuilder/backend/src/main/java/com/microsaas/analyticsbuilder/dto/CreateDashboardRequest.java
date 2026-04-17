package com.microsaas.analyticsbuilder.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Data
public class CreateDashboardRequest {
    @NotBlank
    private String name;
    private String description;
    private String layoutConfig;
    private String whiteLabelConfig;
    private List<DashboardWidgetDto> widgets;
}
