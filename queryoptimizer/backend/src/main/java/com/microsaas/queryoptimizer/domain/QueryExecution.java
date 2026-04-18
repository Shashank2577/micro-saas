package com.microsaas.queryoptimizer.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "query_executions")
public class QueryExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fingerprint_id", nullable = false)
    private QueryFingerprint fingerprint;

    @Column(name = "raw_query", nullable = false, columnDefinition = "TEXT")
    private String rawQuery;

    @Column(name = "execution_time_ms", nullable = false)
    private Double executionTimeMs;

    @Column(name = "database_user")
    private String databaseUser;

    @Column(name = "executed_at", nullable = false)
    private OffsetDateTime executedAt;

    @Column(name = "execution_plan", columnDefinition = "TEXT")
    private String executionPlan;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    public QueryExecution() {}

    public QueryExecution(UUID tenantId, QueryFingerprint fingerprint, String rawQuery, Double executionTimeMs, OffsetDateTime executedAt) {
        this.tenantId = tenantId;
        this.fingerprint = fingerprint;
        this.rawQuery = rawQuery;
        this.executionTimeMs = executionTimeMs;
        this.executedAt = executedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public QueryFingerprint getFingerprint() { return fingerprint; }
    public void setFingerprint(QueryFingerprint fingerprint) { this.fingerprint = fingerprint; }
    public String getRawQuery() { return rawQuery; }
    public void setRawQuery(String rawQuery) { this.rawQuery = rawQuery; }
    public Double getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Double executionTimeMs) { this.executionTimeMs = executionTimeMs; }
    public String getDatabaseUser() { return databaseUser; }
    public void setDatabaseUser(String databaseUser) { this.databaseUser = databaseUser; }
    public OffsetDateTime getExecutedAt() { return executedAt; }
    public void setExecutedAt(OffsetDateTime executedAt) { this.executedAt = executedAt; }
    public String getExecutionPlan() { return executionPlan; }
    public void setExecutionPlan(String executionPlan) { this.executionPlan = executionPlan; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
