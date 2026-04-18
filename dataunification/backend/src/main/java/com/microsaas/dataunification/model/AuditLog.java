package com.microsaas.dataunification.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLog {
    @Id
    private UUID id;
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
    @Column(name = "job_id")
    private UUID jobId;
    @Column(name = "record_id")
    private String recordId;
    @Column(name = "action")
    private String action;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "details")
    private String details;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public AuditLog() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public UUID getJobId() { return jobId; }
    public void setJobId(UUID jobId) { this.jobId = jobId; }
    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
