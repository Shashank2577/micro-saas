package com.microsaas.datalineagetracker.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class AuditEvaluationRequest {
    private UUID assetId;
    private UUID userId;
    private String action; // READ, EXPORT, MODIFY
}
