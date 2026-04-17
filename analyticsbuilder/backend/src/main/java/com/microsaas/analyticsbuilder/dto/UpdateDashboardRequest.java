package com.microsaas.analyticsbuilder.dto;

import lombok.Data;
import java.util.List;

@Data
public class UpdateDashboardRequest {
    private String name;
    private String description;
    private String layoutConfig;
    private String whiteLabelConfig;
    private List<DashboardWidgetDto> widgets;
}
