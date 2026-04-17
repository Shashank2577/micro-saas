package com.microsaas.datalineagetracker.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class LineageLinkDto {
    private UUID sourceAssetId;
    private UUID targetAssetId;
    private String transformationLogic;
}
