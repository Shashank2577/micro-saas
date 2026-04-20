package com.microsaas.contextlayer.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "customer_preferences", indexes = {
    @Index(name = "idx_customer_prefkey", columnList = "customer_id, preference_key")
})
public class CustomerPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "preference_key", nullable = false)
    private String preferenceKey;

    @Column(name = "preference_value")
    private String preferenceValue;

    @Column(name = "source_app")
    private String sourceApp;

    @Column(name = "valid_from")
    private Instant validFrom;

    @Column(name = "valid_until")
    private Instant validUntil;

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
    public String getPreferenceKey() {
        return this.preferenceKey;
    }
    public void setPreferenceKey(String preferenceKey) {
        this.preferenceKey = preferenceKey;
    }
    public String getPreferenceValue() {
        return this.preferenceValue;
    }
    public void setPreferenceValue(String preferenceValue) {
        this.preferenceValue = preferenceValue;
    }
    public String getSourceApp() {
        return this.sourceApp;
    }
    public void setSourceApp(String sourceApp) {
        this.sourceApp = sourceApp;
    }
    public Instant getValidFrom() {
        return this.validFrom;
    }
    public void setValidFrom(Instant validFrom) {
        this.validFrom = validFrom;
    }
    public Instant getValidUntil() {
        return this.validUntil;
    }
    public void setValidUntil(Instant validUntil) {
        this.validUntil = validUntil;
    }
}
