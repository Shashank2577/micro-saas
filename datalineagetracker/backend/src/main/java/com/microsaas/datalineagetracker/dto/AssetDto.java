package com.microsaas.datalineagetracker.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class AssetDto {
    private String name;
    private String type;
    private String sourceSystem;
    private UUID ownerId;
    private UUID stewardId;
    private String classification;
    private String description;
    private Integer retentionDays;
}
