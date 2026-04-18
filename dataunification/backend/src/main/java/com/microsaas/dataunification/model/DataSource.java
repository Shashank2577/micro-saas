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
@Table(name = "data_source")
public class DataSource {
    @Id
    private UUID id;
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String type;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "connection_details")
    private String connectionDetails;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public DataSource() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getConnectionDetails() { return connectionDetails; }
    public void setConnectionDetails(String connectionDetails) { this.connectionDetails = connectionDetails; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
