package com.microsaas.contextlayer.dto;


import java.time.Instant;
import java.util.UUID;


public class CustomerContextDTO {
    private String customerId;
    private String profile;
    private String preferences;
    private String attributes;
    private Instant lastUpdatedAt;
    private String updatedByApp;

    public String getCustomerId() {
        return this.customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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
