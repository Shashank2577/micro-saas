package com.microsaas.workspacemanager.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String action;

    @Column(name = "actor_id")
    private UUID actorId;

    @Column(name = "target_id")
    private UUID targetId;

    @Column(columnDefinition = "jsonb")
    private String details;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public UUID getId() { return this.id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getTenantId() { return this.tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }

    public String getAction() { return this.action; }
    public void setAction(String action) { this.action = action; }

    public UUID getActorId() { return this.actorId; }
    public void setActorId(UUID actorId) { this.actorId = actorId; }

    public UUID getTargetId() { return this.targetId; }
    public void setTargetId(UUID targetId) { this.targetId = targetId; }

    public String getDetails() { return this.details; }
    public void setDetails(String details) { this.details = details; }

    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public AuditLog() {}
}
