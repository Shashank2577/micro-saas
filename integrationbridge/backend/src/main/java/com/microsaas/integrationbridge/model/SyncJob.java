package com.microsaas.integrationbridge.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "sync_jobs")
public class SyncJob {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "integration_id", nullable = false)
    private UUID integrationId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "schedule_cron", nullable = false)
    private String scheduleCron;

    @Column(name = "source_entity", nullable = false)
    private String sourceEntity;

    @Column(name = "target_entity", nullable = false)
    private String targetEntity;

    @Column(nullable = false)
    private String status;

    @Column(name = "last_run_at")
    private LocalDateTime lastRunAt;

    @Column(name = "next_run_at")
    private LocalDateTime nextRunAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getIntegrationId() { return integrationId; }
    public void setIntegrationId(UUID integrationId) { this.integrationId = integrationId; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getScheduleCron() { return scheduleCron; }
    public void setScheduleCron(String scheduleCron) { this.scheduleCron = scheduleCron; }
    public String getSourceEntity() { return sourceEntity; }
    public void setSourceEntity(String sourceEntity) { this.sourceEntity = sourceEntity; }
    public String getTargetEntity() { return targetEntity; }
    public void setTargetEntity(String targetEntity) { this.targetEntity = targetEntity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getLastRunAt() { return lastRunAt; }
    public void setLastRunAt(LocalDateTime lastRunAt) { this.lastRunAt = lastRunAt; }
    public LocalDateTime getNextRunAt() { return nextRunAt; }
    public void setNextRunAt(LocalDateTime nextRunAt) { this.nextRunAt = nextRunAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
