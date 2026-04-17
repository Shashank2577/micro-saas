package com.microsaas.datagovernance.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "data_lineage_node")
public class DataLineageNode {
    @Id
    private UUID id;
    private UUID tenantId;
    private String fieldName;
    private String originService;
    private String currentService;
    private String transformationLogic;
    private LocalDateTime createdAt;

    public DataLineageNode() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }
    public String getOriginService() { return originService; }
    public void setOriginService(String originService) { this.originService = originService; }
    public String getCurrentService() { return currentService; }
    public void setCurrentService(String currentService) { this.currentService = currentService; }
    public String getTransformationLogic() { return transformationLogic; }
    public void setTransformationLogic(String transformationLogic) { this.transformationLogic = transformationLogic; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
