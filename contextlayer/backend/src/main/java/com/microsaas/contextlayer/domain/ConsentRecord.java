package com.microsaas.contextlayer.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "consent_records", indexes = {
    @Index(name = "idx_customer_consent_type", columnList = "customer_id, consent_type")
})
public class ConsentRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "consent_type", nullable = false)
    private String consentType;

    @Column(name = "granted", nullable = false)
    private Boolean granted;

    @Column(name = "consented_at")
    private Instant consentedAt;

    @Column(name = "consented_by_app")
    private String consentedByApp;

    @Column(name = "expiry_date")
    private Instant expiryDate;

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
    public String getConsentType() {
        return this.consentType;
    }
    public void setConsentType(String consentType) {
        this.consentType = consentType;
    }
    public Boolean getGranted() {
        return this.granted;
    }
    public void setGranted(Boolean granted) {
        this.granted = granted;
    }
    public Instant getConsentedAt() {
        return this.consentedAt;
    }
    public void setConsentedAt(Instant consentedAt) {
        this.consentedAt = consentedAt;
    }
    public String getConsentedByApp() {
        return this.consentedByApp;
    }
    public void setConsentedByApp(String consentedByApp) {
        this.consentedByApp = consentedByApp;
    }
    public Instant getExpiryDate() {
        return this.expiryDate;
    }
    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }
}
