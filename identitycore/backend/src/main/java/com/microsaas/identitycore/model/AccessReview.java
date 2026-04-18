package com.microsaas.identitycore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "access_reviews")
public class AccessReview {
    @Id
    private UUID id;
    private UUID tenantId;
    private UUID reviewerId;
    private UUID targetUserId;
    private OffsetDateTime reviewPeriodStart;
    private OffsetDateTime reviewPeriodEnd;
    private String status;
    private String aiRecommendation;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public AccessReview() {}

    public AccessReview(UUID id, UUID tenantId, UUID reviewerId, UUID targetUserId, OffsetDateTime reviewPeriodStart, OffsetDateTime reviewPeriodEnd, String status, String aiRecommendation, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.reviewerId = reviewerId;
        this.targetUserId = targetUserId;
        this.reviewPeriodStart = reviewPeriodStart;
        this.reviewPeriodEnd = reviewPeriodEnd;
        this.status = status;
        this.aiRecommendation = aiRecommendation;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }

    public UUID getReviewerId() { return reviewerId; }
    public void setReviewerId(UUID reviewerId) { this.reviewerId = reviewerId; }

    public UUID getTargetUserId() { return targetUserId; }
    public void setTargetUserId(UUID targetUserId) { this.targetUserId = targetUserId; }

    public OffsetDateTime getReviewPeriodStart() { return reviewPeriodStart; }
    public void setReviewPeriodStart(OffsetDateTime reviewPeriodStart) { this.reviewPeriodStart = reviewPeriodStart; }

    public OffsetDateTime getReviewPeriodEnd() { return reviewPeriodEnd; }
    public void setReviewPeriodEnd(OffsetDateTime reviewPeriodEnd) { this.reviewPeriodEnd = reviewPeriodEnd; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAiRecommendation() { return aiRecommendation; }
    public void setAiRecommendation(String aiRecommendation) { this.aiRecommendation = aiRecommendation; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
