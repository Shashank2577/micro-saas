package com.microsaas.contextlayer.dto;


import java.time.Instant;
import java.util.UUID;


public class InteractionHistoryDTO {
    private UUID interactionId;
    private String customerId;
    private String appId;
    private String interactionType;
    private Instant timestamp;
    private String metadata;
    private String outcomes;

    public UUID getInteractionId() {
        return this.interactionId;
    }
    public void setInteractionId(UUID interactionId) {
        this.interactionId = interactionId;
    }
    public String getCustomerId() {
        return this.customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getAppId() {
        return this.appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getInteractionType() {
        return this.interactionType;
    }
    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }
    public Instant getTimestamp() {
        return this.timestamp;
    }
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
    public String getMetadata() {
        return this.metadata;
    }
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
    public String getOutcomes() {
        return this.outcomes;
    }
    public void setOutcomes(String outcomes) {
        this.outcomes = outcomes;
    }
}
