package com.microsaas.queryoptimizer.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "query_fingerprints", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tenant_id", "fingerprint_hash"})
})
public class QueryFingerprint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "fingerprint_hash", nullable = false)
    private String fingerprintHash;

    @Column(name = "normalized_query", nullable = false, columnDefinition = "TEXT")
    private String normalizedQuery;

    @Column(name = "database_type", nullable = false)
    private String databaseType;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public QueryFingerprint() {}

    public QueryFingerprint(UUID tenantId, String fingerprintHash, String normalizedQuery, String databaseType) {
        this.tenantId = tenantId;
        this.fingerprintHash = fingerprintHash;
        this.normalizedQuery = normalizedQuery;
        this.databaseType = databaseType;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getFingerprintHash() { return fingerprintHash; }
    public void setFingerprintHash(String fingerprintHash) { this.fingerprintHash = fingerprintHash; }
    public String getNormalizedQuery() { return normalizedQuery; }
    public void setNormalizedQuery(String normalizedQuery) { this.normalizedQuery = normalizedQuery; }
    public String getDatabaseType() { return databaseType; }
    public void setDatabaseType(String databaseType) { this.databaseType = databaseType; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
