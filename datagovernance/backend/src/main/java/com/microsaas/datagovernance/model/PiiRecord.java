package com.microsaas.datagovernance.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pii_record")
public class PiiRecord {
    @Id
    private UUID id;
    private UUID tenantId;
    private String sourceTable;
    private String sourceColumn;
    private String piiType;
    private Boolean isMasked;
    private LocalDateTime createdAt;

    public PiiRecord() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getSourceTable() { return sourceTable; }
    public void setSourceTable(String sourceTable) { this.sourceTable = sourceTable; }
    public String getSourceColumn() { return sourceColumn; }
    public void setSourceColumn(String sourceColumn) { this.sourceColumn = sourceColumn; }
    public String getPiiType() { return piiType; }
    public void setPiiType(String piiType) { this.piiType = piiType; }
    public Boolean getMasked() { return isMasked; }
    public void setMasked(Boolean masked) { isMasked = masked; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
