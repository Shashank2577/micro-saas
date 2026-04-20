package com.microsaas.contextlayer.dto;


import java.time.Instant;
import java.util.UUID;


public class CustomerPreferenceDTO {
    private String customerId;
    private String preferenceKey;
    private String preferenceValue;
    private String sourceApp;
    private Instant validFrom;
    private Instant validUntil;

    public String getCustomerId() {
        return this.customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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
