package com.microsaas.identitycore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "access_logs")
public class AccessLog {
    @Id
    private UUID id;
    private UUID tenantId;
    private UUID userId;
    private String resourceAccessed;
    private String action;
    private String ipAddress;
    private OffsetDateTime accessTime;
    private String status;
    private OffsetDateTime createdAt;

    public AccessLog() {}

    public AccessLog(UUID id, UUID tenantId, UUID userId, String resourceAccessed, String action, String ipAddress, OffsetDateTime accessTime, String status, OffsetDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.userId = userId;
        this.resourceAccessed = resourceAccessed;
        this.action = action;
        this.ipAddress = ipAddress;
        this.accessTime = accessTime;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getResourceAccessed() { return resourceAccessed; }
    public void setResourceAccessed(String resourceAccessed) { this.resourceAccessed = resourceAccessed; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public OffsetDateTime getAccessTime() { return accessTime; }
    public void setAccessTime(OffsetDateTime accessTime) { this.accessTime = accessTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
