package com.tenantmanager.dto;

import lombok.Data;

@Data
public class CreateMilestoneRequest {
    private String title;
    private String description;
}
