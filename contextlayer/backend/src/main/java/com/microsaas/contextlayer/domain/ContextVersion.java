package com.microsaas.contextlayer.domain;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "context_versions", indexes = {
    @Index(name = "idx_customer_created", columnList = "customer_id, created_at")
})
public class ContextVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "version_id")
    private UUID versionId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String contextSnapshot;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by_app")
    private String createdByApp;

    @Column(name = "change_description")
    private String changeDescription;

    public UUID getVersionId() {
        return this.versionId;
    }
    public void setVersionId(UUID versionId) {
        this.versionId = versionId;
    }
    public String getCustomerId() {
        return this.customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public UUID getTenantId() {
        return this.tenantId;
    }
    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }
    public String getContextSnapshot() {
        return this.contextSnapshot;
    }
    public void setContextSnapshot(String contextSnapshot) {
        this.contextSnapshot = contextSnapshot;
    }
    public Instant getCreatedAt() {
        return this.createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    public String getCreatedByApp() {
        return this.createdByApp;
    }
    public void setCreatedByApp(String createdByApp) {
        this.createdByApp = createdByApp;
    }
    public String getChangeDescription() {
        return this.changeDescription;
    }
    public void setChangeDescription(String changeDescription) {
        this.changeDescription = changeDescription;
    }
}
