package com.microsaas.identitycore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "privileges")
public class Privilege {
    @Id
    private UUID id;
    private UUID tenantId;
    private UUID userId;
    private String resourceName;
    private String permissionLevel;
    private OffsetDateTime lastUsedAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public Privilege() {}

    public Privilege(UUID id, UUID tenantId, UUID userId, String resourceName, String permissionLevel, OffsetDateTime lastUsedAt, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.userId = userId;
        this.resourceName = resourceName;
        this.permissionLevel = permissionLevel;
        this.lastUsedAt = lastUsedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }

    public String getPermissionLevel() { return permissionLevel; }
    public void setPermissionLevel(String permissionLevel) { this.permissionLevel = permissionLevel; }

    public OffsetDateTime getLastUsedAt() { return lastUsedAt; }
    public void setLastUsedAt(OffsetDateTime lastUsedAt) { this.lastUsedAt = lastUsedAt; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
