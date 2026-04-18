package com.microsaas.apimanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "api_analytics")
public class ApiAnalytics {
    @Id
    private UUID id;
    private String tenantId;
    private UUID projectId;
    private UUID endpointId;
    private String path;
    private String method;
    private Integer statusCode;
    private Integer latencyMs;
    private LocalDateTime timestamp;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public UUID getProjectId() { return projectId; }
    public void setProjectId(UUID projectId) { this.projectId = projectId; }
    public UUID getEndpointId() { return endpointId; }
    public void setEndpointId(UUID endpointId) { this.endpointId = endpointId; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public Integer getStatusCode() { return statusCode; }
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }
    public Integer getLatencyMs() { return latencyMs; }
    public void setLatencyMs(Integer latencyMs) { this.latencyMs = latencyMs; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
