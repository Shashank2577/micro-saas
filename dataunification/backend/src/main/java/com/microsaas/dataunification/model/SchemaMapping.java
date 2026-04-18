package com.microsaas.dataunification.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "schema_mapping")
public class SchemaMapping {
    @Id
    private UUID id;
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
    @Column(nullable = false)
    private String name;
    @Column(name = "source_id")
    private UUID sourceId;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "mapping_rules")
    private String mappingRules;
    @Column(nullable = false)
    private Integer version = 1;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public SchemaMapping() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public UUID getSourceId() { return sourceId; }
    public void setSourceId(UUID sourceId) { this.sourceId = sourceId; }
    public String getMappingRules() { return mappingRules; }
    public void setMappingRules(String mappingRules) { this.mappingRules = mappingRules; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
