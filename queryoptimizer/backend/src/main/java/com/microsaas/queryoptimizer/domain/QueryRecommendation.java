package com.microsaas.queryoptimizer.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "query_recommendations")
public class QueryRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fingerprint_id", nullable = false)
    private QueryFingerprint fingerprint;

    @Column(name = "recommendation_type", nullable = false)
    private String recommendationType;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "confidence_score", nullable = false)
    private Double confidenceScore;

    @Column(name = "impact_estimate", nullable = false, columnDefinition = "TEXT")
    private String impactEstimate;

    @Column(name = "status", nullable = false)
    private String status = "PENDING";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public QueryRecommendation() {}

    public QueryRecommendation(UUID tenantId, QueryFingerprint fingerprint, String recommendationType, String description, Double confidenceScore, String impactEstimate) {
        this.tenantId = tenantId;
        this.fingerprint = fingerprint;
        this.recommendationType = recommendationType;
        this.description = description;
        this.confidenceScore = confidenceScore;
        this.impactEstimate = impactEstimate;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public QueryFingerprint getFingerprint() { return fingerprint; }
    public void setFingerprint(QueryFingerprint fingerprint) { this.fingerprint = fingerprint; }
    public String getRecommendationType() { return recommendationType; }
    public void setRecommendationType(String recommendationType) { this.recommendationType = recommendationType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }
    public String getImpactEstimate() { return impactEstimate; }
    public void setImpactEstimate(String impactEstimate) { this.impactEstimate = impactEstimate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
