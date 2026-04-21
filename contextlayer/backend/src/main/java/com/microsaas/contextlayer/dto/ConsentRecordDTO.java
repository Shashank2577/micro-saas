package com.microsaas.contextlayer.dto;


import java.time.Instant;
import java.util.UUID;


public class ConsentRecordDTO {
    private String customerId;
    private String consentType;
    private Boolean granted;
    private Instant consentedAt;
    private String consentedByApp;
    private Instant expiryDate;

    public String getCustomerId() {
        return this.customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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
