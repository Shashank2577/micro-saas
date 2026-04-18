package com.microsaas.cacheoptimizer.policy;

import java.util.UUID;

public class CachePolicyDto {
    private UUID id;
    private String appName;
    private String namespace;
    private Long ttlSeconds;
    private CacheStrategy strategy;
    private Boolean compressionEnabled;
    private Boolean staleWhileRevalidate;
    private Long staleTtlSeconds;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
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
}
