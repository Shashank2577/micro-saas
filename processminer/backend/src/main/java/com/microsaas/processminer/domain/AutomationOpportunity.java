package com.microsaas.processminer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;

@Entity
@Table(name = "automation_opportunities")
public class AutomationOpportunity {

    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "process_model_id", nullable = false)
    private UUID processModelId;

    @Column(name = "activity_name", nullable = false)
    private String activityName;

    @Column(name = "estimated_roi")
    private BigDecimal estimatedRoi;

    @Column(name = "effort_estimate")
    private String effortEstimate;

    @Column(name = "rationale", columnDefinition = "text")
    private String rationale;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public UUID getProcessModelId() { return processModelId; }
    public void setProcessModelId(UUID processModelId) { this.processModelId = processModelId; }
    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }
    public BigDecimal getEstimatedRoi() { return estimatedRoi; }
    public void setEstimatedRoi(BigDecimal estimatedRoi) { this.estimatedRoi = estimatedRoi; }
    public String getEffortEstimate() { return effortEstimate; }
    public void setEffortEstimate(String effortEstimate) { this.effortEstimate = effortEstimate; }
    public String getRationale() { return rationale; }
    public void setRationale(String rationale) { this.rationale = rationale; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
