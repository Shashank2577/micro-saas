package com.microsaas.identitycore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "anomalies")
public class Anomaly {
    @Id
    private UUID id;
    private UUID tenantId;
    private UUID userId;
    private UUID accessLogId;
    private String anomalyType;
    private String severity;
    private String description;
    private String status;
    private OffsetDateTime detectedAt;
    private OffsetDateTime resolvedAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public Anomaly() {}

    public Anomaly(UUID id, UUID tenantId, UUID userId, UUID accessLogId, String anomalyType, String severity, String description, String status, OffsetDateTime detectedAt, OffsetDateTime resolvedAt, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.userId = userId;
        this.accessLogId = accessLogId;
        this.anomalyType = anomalyType;
        this.severity = severity;
        this.description = description;
        this.status = status;
        this.detectedAt = detectedAt;
        this.resolvedAt = resolvedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public UUID getAccessLogId() { return accessLogId; }
    public void setAccessLogId(UUID accessLogId) { this.accessLogId = accessLogId; }

    public String getAnomalyType() { return anomalyType; }
    public void setAnomalyType(String anomalyType) { this.anomalyType = anomalyType; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public OffsetDateTime getDetectedAt() { return detectedAt; }
    public void setDetectedAt(OffsetDateTime detectedAt) { this.detectedAt = detectedAt; }

    public OffsetDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(OffsetDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
