package com.microsaas.apimanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "api_versions")
public class ApiVersion {
    @Id
    private UUID id;
    private String tenantId;
    private UUID projectId;
    private String versionString;
    private String openapiSchema;
    private String status;
    private String releaseNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public UUID getProjectId() { return projectId; }
    public void setProjectId(UUID projectId) { this.projectId = projectId; }
    public String getVersionString() { return versionString; }
    public void setVersionString(String versionString) { this.versionString = versionString; }
    public String getOpenapiSchema() { return openapiSchema; }
    public void setOpenapiSchema(String openapiSchema) { this.openapiSchema = openapiSchema; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReleaseNotes() { return releaseNotes; }
    public void setReleaseNotes(String releaseNotes) { this.releaseNotes = releaseNotes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
