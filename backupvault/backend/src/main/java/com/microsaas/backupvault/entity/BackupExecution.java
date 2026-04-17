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
@Table(name = "backup_executions")
public class BackupExecution {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @ManyToOne
    @JoinColumn(name = "policy_id")
    private BackupPolicy policy;

    @Column(nullable = false)
    private String status;

    @Column(name = "backup_type", nullable = false)
    private String backupType;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "original_size_bytes")
    private Long originalSizeBytes;

    @Column(name = "compressed_size_bytes")
    private Long compressedSizeBytes;

    @Column(name = "storage_path")
    private String storagePath;

    @Column(name = "replication_status")
    private String replicationStatus;

    @Column(name = "encryption_verified")
    private Boolean encryptionVerified = false;

    @Column(name = "integrity_verified")
    private Boolean integrityVerified = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public BackupPolicy getPolicy() { return policy; }
    public void setPolicy(BackupPolicy policy) { this.policy = policy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getBackupType() { return backupType; }
    public void setBackupType(String backupType) { this.backupType = backupType; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public Long getOriginalSizeBytes() { return originalSizeBytes; }
    public void setOriginalSizeBytes(Long originalSizeBytes) { this.originalSizeBytes = originalSizeBytes; }
    public Long getCompressedSizeBytes() { return compressedSizeBytes; }
    public void setCompressedSizeBytes(Long compressedSizeBytes) { this.compressedSizeBytes = compressedSizeBytes; }
    public String getStoragePath() { return storagePath; }
    public void setStoragePath(String storagePath) { this.storagePath = storagePath; }
    public String getReplicationStatus() { return replicationStatus; }
    public void setReplicationStatus(String replicationStatus) { this.replicationStatus = replicationStatus; }
    public Boolean getEncryptionVerified() { return encryptionVerified; }
    public void setEncryptionVerified(Boolean encryptionVerified) { this.encryptionVerified = encryptionVerified; }
    public Boolean getIntegrityVerified() { return integrityVerified; }
    public void setIntegrityVerified(Boolean integrityVerified) { this.integrityVerified = integrityVerified; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
