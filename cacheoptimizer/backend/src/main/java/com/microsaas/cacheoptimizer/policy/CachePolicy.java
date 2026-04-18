package com.microsaas.cacheoptimizer.policy;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "cache_policies")
public class CachePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "app_name", nullable = false)
    private String appName;

    @Column(nullable = false)
    private String namespace;

    @Column(name = "ttl_seconds", nullable = false)
    private Long ttlSeconds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CacheStrategy strategy;

    @Column(name = "compression_enabled")
    private Boolean compressionEnabled = false;

    @Column(name = "stale_while_revalidate")
    private Boolean staleWhileRevalidate = false;

    @Column(name = "stale_ttl_seconds")
    private Long staleTtlSeconds;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public CachePolicy() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
    public String getNamespace() { return namespace; }
    public void setNamespace(String namespace) { this.namespace = namespace; }
    public Long getTtlSeconds() { return ttlSeconds; }
    public void setTtlSeconds(Long ttlSeconds) { this.ttlSeconds = ttlSeconds; }
    public CacheStrategy getStrategy() { return strategy; }
    public void setStrategy(CacheStrategy strategy) { this.strategy = strategy; }
    public Boolean getCompressionEnabled() { return compressionEnabled; }
    public void setCompressionEnabled(Boolean compressionEnabled) { this.compressionEnabled = compressionEnabled; }
    public Boolean getStaleWhileRevalidate() { return staleWhileRevalidate; }
    public void setStaleWhileRevalidate(Boolean staleWhileRevalidate) { this.staleWhileRevalidate = staleWhileRevalidate; }
    public Long getStaleTtlSeconds() { return staleTtlSeconds; }
    public void setStaleTtlSeconds(Long staleTtlSeconds) { this.staleTtlSeconds = staleTtlSeconds; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
