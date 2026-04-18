package com.microsaas.dataunification.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "sync_job")
public class SyncJob {
    @Id
    private UUID id;
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
    @Column(name = "mapping_id")
    private UUID mappingId;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private String type;
    @Column(name = "records_processed")
    private Integer recordsProcessed = 0;
    @Column(name = "error_log", columnDefinition = "TEXT")
    private String errorLog;
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public SyncJob() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public UUID getMappingId() { return mappingId; }
    public void setMappingId(UUID mappingId) { this.mappingId = mappingId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getRecordsProcessed() { return recordsProcessed; }
    public void setRecordsProcessed(Integer recordsProcessed) { this.recordsProcessed = recordsProcessed; }
    public String getErrorLog() { return errorLog; }
    public void setErrorLog(String errorLog) { this.errorLog = errorLog; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
