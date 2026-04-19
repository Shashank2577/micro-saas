package com.microsaas.onboardflow.model;
import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp; import org.hibernate.annotations.UpdateTimestamp; import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
@Entity
@Table(name = "onboarding_plans")
public class OnboardingPlan {
    @Id private UUID id;
    @Column(name = "tenant_id", nullable = false) private UUID tenantId;
    @Column(nullable = false, length = 180) private String name;
    @Column(nullable = false, length = 40) private String status;
    @JdbcTypeCode(SqlTypes.JSON) @Column(name = "metadata_json", columnDefinition = "jsonb") private String metadataJson;
    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false) private ZonedDateTime createdAt;
    @UpdateTimestamp @Column(name = "updated_at", nullable = false) private ZonedDateTime updatedAt;
    public UUID getId() { return id; } public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; } public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public String getMetadataJson() { return metadataJson; } public void setMetadataJson(String metadataJson) { this.metadataJson = metadataJson; }
    public ZonedDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; } public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }
}
