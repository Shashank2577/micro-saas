package com.microsaas.contextlayer.dto;


import java.time.Instant;
import java.util.UUID;


public class ContextVersionDTO {
    private UUID versionId;
    private String customerId;
    private String contextSnapshot;
    private Instant createdAt;
    private String createdByApp;
    private String changeDescription;

    public UUID getVersionId() {
        return this.versionId;
    }
    public void setVersionId(UUID versionId) {
        this.versionId = versionId;
    }
    public String getCustomerId() {
        return this.customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getContextSnapshot() {
        return this.contextSnapshot;
    }
    public void setContextSnapshot(String contextSnapshot) {
        this.contextSnapshot = contextSnapshot;
    }
    public Instant getCreatedAt() {
        return this.createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    public String getCreatedByApp() {
        return this.createdByApp;
    }
    public void setCreatedByApp(String createdByApp) {
        this.createdByApp = createdByApp;
    }
    public String getChangeDescription() {
        return this.changeDescription;
    }
    public void setChangeDescription(String changeDescription) {
        this.changeDescription = changeDescription;
    }
}
