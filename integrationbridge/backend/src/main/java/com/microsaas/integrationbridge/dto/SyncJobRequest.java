package com.microsaas.integrationbridge.dto;

public class SyncJobRequest {
    private String scheduleCron;
    private String sourceEntity;
    private String targetEntity;

    public String getScheduleCron() { return scheduleCron; }
    public void setScheduleCron(String scheduleCron) { this.scheduleCron = scheduleCron; }
    public String getSourceEntity() { return sourceEntity; }
    public void setSourceEntity(String sourceEntity) { this.sourceEntity = sourceEntity; }
    public String getTargetEntity() { return targetEntity; }
    public void setTargetEntity(String targetEntity) { this.targetEntity = targetEntity; }
}
