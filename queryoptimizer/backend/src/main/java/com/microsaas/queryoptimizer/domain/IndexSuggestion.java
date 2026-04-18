package com.microsaas.queryoptimizer.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "index_suggestions")
public class IndexSuggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fingerprint_id", nullable = false)
    private QueryFingerprint fingerprint;

    @Column(name = "table_name", nullable = false)
    private String tableName;

    @Column(name = "columns_suggested", nullable = false, columnDefinition = "TEXT")
    private String columnsSuggested;

    @Column(name = "creation_statement", nullable = false, columnDefinition = "TEXT")
    private String creationStatement;

    @Column(name = "estimated_improvement")
    private Double estimatedImprovement;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    public IndexSuggestion() {}

    public IndexSuggestion(UUID tenantId, QueryFingerprint fingerprint, String tableName, String columnsSuggested, String creationStatement, Double estimatedImprovement) {
        this.tenantId = tenantId;
        this.fingerprint = fingerprint;
        this.tableName = tableName;
        this.columnsSuggested = columnsSuggested;
        this.creationStatement = creationStatement;
        this.estimatedImprovement = estimatedImprovement;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public QueryFingerprint getFingerprint() { return fingerprint; }
    public void setFingerprint(QueryFingerprint fingerprint) { this.fingerprint = fingerprint; }
    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }
    public String getColumnsSuggested() { return columnsSuggested; }
    public void setColumnsSuggested(String columnsSuggested) { this.columnsSuggested = columnsSuggested; }
    public String getCreationStatement() { return creationStatement; }
    public void setCreationStatement(String creationStatement) { this.creationStatement = creationStatement; }
    public Double getEstimatedImprovement() { return estimatedImprovement; }
    public void setEstimatedImprovement(Double estimatedImprovement) { this.estimatedImprovement = estimatedImprovement; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
