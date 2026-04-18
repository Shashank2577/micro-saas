package com.tenantmanager.dto;

import lombok.Data;

@Data
public class CreateEventRequest {
    private String eventType;
    private String description;
}
