package com.microsaas.contextlayer.domain;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "customer_contexts", indexes = {
    @Index(name = "idx_customer_tenant", columnList = "customer_id, tenant_id", unique = true)
})
public class CustomerContext {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String profile;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String preferences;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String attributes;

    @Column(name = "last_updated_at")
    private Instant lastUpdatedAt;

    @Column(name = "updated_by_app")
    private String updatedByApp;

    public UUID getId() {
        return this.id;
    }
    public void setId(UUID id) {
        this.id = id;
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
    public String getProfile() {
        return this.profile;
    }
    public void setProfile(String profile) {
        this.profile = profile;
    }
    public String getPreferences() {
        return this.preferences;
    }
    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
    public String getAttributes() {
        return this.attributes;
    }
    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
    public Instant getLastUpdatedAt() {
        return this.lastUpdatedAt;
    }
    public void setLastUpdatedAt(Instant lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }
    public String getUpdatedByApp() {
        return this.updatedByApp;
    }
    public void setUpdatedByApp(String updatedByApp) {
        this.updatedByApp = updatedByApp;
    }
}
