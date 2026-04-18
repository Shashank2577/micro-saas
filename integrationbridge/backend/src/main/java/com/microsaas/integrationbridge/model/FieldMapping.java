package com.microsaas.integrationbridge.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.util.UUID;

@Entity
@Table(name = "field_mappings")
public class FieldMapping {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "sync_job_id", nullable = false)
    private UUID syncJobId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "source_field", nullable = false)
    private String sourceField;

    @Column(name = "target_field", nullable = false)
    private String targetField;

    @Column(name = "transformation_rule", length = 1024)
    private String transformationRule;

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getSyncJobId() { return syncJobId; }
    public void setSyncJobId(UUID syncJobId) { this.syncJobId = syncJobId; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getSourceField() { return sourceField; }
    public void setSourceField(String sourceField) { this.sourceField = sourceField; }
    public String getTargetField() { return targetField; }
    public void setTargetField(String targetField) { this.targetField = targetField; }
    public String getTransformationRule() { return transformationRule; }
    public void setTransformationRule(String transformationRule) { this.transformationRule = transformationRule; }
}
