package com.ecosystemmap.dto;

import java.util.UUID;


public class DataFlowDto {
    private UUID sourceAppId;
    private UUID targetAppId;
    private String eventType;
    private String healthStatus;

    public UUID getSourceAppId() {
        return sourceAppId;
    }

    public void setSourceAppId(UUID sourceAppId) {
        this.sourceAppId = sourceAppId;
    }

    public UUID getTargetAppId() {
        return targetAppId;
    }

    public void setTargetAppId(UUID targetAppId) {
        this.targetAppId = targetAppId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }
}
