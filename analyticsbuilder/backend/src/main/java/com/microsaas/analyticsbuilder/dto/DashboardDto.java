package com.microsaas.analyticsbuilder.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Data
public class DashboardDto {
    private UUID id;
    private String name;
    private String description;
    private String layoutConfig;
    private String whiteLabelConfig;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private List<DashboardWidgetDto> widgets;
}
