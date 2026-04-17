package com.microsaas.backupvault.dto;

import java.util.UUID;

public class BackupExecutionDto {
    private UUID policyId;
    private String backupType;

    public UUID getPolicyId() { return policyId; }
    public void setPolicyId(UUID policyId) { this.policyId = policyId; }
    public String getBackupType() { return backupType; }
    public void setBackupType(String backupType) { this.backupType = backupType; }
}
