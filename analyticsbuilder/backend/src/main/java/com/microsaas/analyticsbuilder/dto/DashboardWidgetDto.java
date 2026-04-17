package com.microsaas.analyticsbuilder.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class DashboardWidgetDto {
    private UUID id;
    private String type;
    private String title;
    private String config;
    private Integer positionX;
    private Integer positionY;
    private Integer width;
    private Integer height;
    private UUID queryId;
}
