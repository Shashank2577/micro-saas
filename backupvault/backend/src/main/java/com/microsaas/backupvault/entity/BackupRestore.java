package com.microsaas.backupvault.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "backup_restores")
public class BackupRestore {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @ManyToOne
    @JoinColumn(name = "execution_id")
    private BackupExecution execution;

    @Column(nullable = false)
    private String status;

    @Column(name = "target_environment", nullable = false)
    private String targetEnvironment;

    @Column(name = "point_in_time")
    private LocalDateTime pointInTime;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public BackupExecution getExecution() { return execution; }
    public void setExecution(BackupExecution execution) { this.execution = execution; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTargetEnvironment() { return targetEnvironment; }
    public void setTargetEnvironment(String targetEnvironment) { this.targetEnvironment = targetEnvironment; }
    public LocalDateTime getPointInTime() { return pointInTime; }
    public void setPointInTime(LocalDateTime pointInTime) { this.pointInTime = pointInTime; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
