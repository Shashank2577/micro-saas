package com.microsaas.workspacemanager.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "workspaces")
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false, unique = true)
    private UUID tenantId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String subdomain;

    @Column(nullable = false)
    private String status; // ACTIVE, SUSPENDED

    @Column(name = "capacity_limit")
    private Integer capacityLimit;

    @Column(name = "branding_logo_url")
    private String brandingLogoUrl;

    @Column(columnDefinition = "jsonb")
    private String features;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;

    public UUID getId() { return this.id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getTenantId() { return this.tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public String getSubdomain() { return this.subdomain; }
    public void setSubdomain(String subdomain) { this.subdomain = subdomain; }

    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getCapacityLimit() { return this.capacityLimit; }
    public void setCapacityLimit(Integer capacityLimit) { this.capacityLimit = capacityLimit; }

    public String getBrandingLogoUrl() { return this.brandingLogoUrl; }
    public void setBrandingLogoUrl(String brandingLogoUrl) { this.brandingLogoUrl = brandingLogoUrl; }

    public String getFeatures() { return this.features; }
    public void setFeatures(String features) { this.features = features; }

    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return this.updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public UUID getCreatedBy() { return this.createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }

    public UUID getUpdatedBy() { return this.updatedBy; }
    public void setUpdatedBy(UUID updatedBy) { this.updatedBy = updatedBy; }

    public Workspace() {}
}
