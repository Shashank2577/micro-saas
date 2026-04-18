package com.microsaas.workspacemanager.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "workspace_members")
public class WorkspaceMember {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role; // ADMIN, MEMBER, GUEST

    @Column(nullable = false)
    private String status; // ACTIVE, INACTIVE, DEPROVISIONED

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "active_sessions")
    private Integer activeSessions = 0;

    @Column(name = "api_key_usage_count")
    private Integer apiKeyUsageCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public UUID getId() { return this.id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getTenantId() { return this.tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }

    public UUID getUserId() { return this.userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return this.role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getLastLoginAt() { return this.lastLoginAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }

    public Integer getActiveSessions() { return this.activeSessions; }
    public void setActiveSessions(Integer activeSessions) { this.activeSessions = activeSessions; }

    public Integer getApiKeyUsageCount() { return this.apiKeyUsageCount; }
    public void setApiKeyUsageCount(Integer apiKeyUsageCount) { this.apiKeyUsageCount = apiKeyUsageCount; }

    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return this.updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public WorkspaceMember() {}
}
