package com.microsaas.backupvault.dto;

import java.util.UUID;
import java.time.LocalDateTime;

public class BackupRestoreDto {
    private UUID executionId;
    private String targetEnvironment;
    private LocalDateTime pointInTime;

    public UUID getExecutionId() { return executionId; }
    public void setExecutionId(UUID executionId) { this.executionId = executionId; }
    public String getTargetEnvironment() { return targetEnvironment; }
    public void setTargetEnvironment(String targetEnvironment) { this.targetEnvironment = targetEnvironment; }
    public LocalDateTime getPointInTime() { return pointInTime; }
    public void setPointInTime(LocalDateTime pointInTime) { this.pointInTime = pointInTime; }
}
