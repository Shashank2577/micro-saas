package com.microsaas.backupvault.dto;

public class BackupPolicyDto {
    private String name;
    private String scheduleCron;
    private Integer retentionDays;
    private String encryptionKeyId;
    private Boolean crossRegionReplication;
    private String targetRegion;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getScheduleCron() { return scheduleCron; }
    public void setScheduleCron(String scheduleCron) { this.scheduleCron = scheduleCron; }
    public Integer getRetentionDays() { return retentionDays; }
    public void setRetentionDays(Integer retentionDays) { this.retentionDays = retentionDays; }
    public String getEncryptionKeyId() { return encryptionKeyId; }
    public void setEncryptionKeyId(String encryptionKeyId) { this.encryptionKeyId = encryptionKeyId; }
    public Boolean getCrossRegionReplication() { return crossRegionReplication; }
    public void setCrossRegionReplication(Boolean crossRegionReplication) { this.crossRegionReplication = crossRegionReplication; }
    public String getTargetRegion() { return targetRegion; }
    public void setTargetRegion(String targetRegion) { this.targetRegion = targetRegion; }
}
