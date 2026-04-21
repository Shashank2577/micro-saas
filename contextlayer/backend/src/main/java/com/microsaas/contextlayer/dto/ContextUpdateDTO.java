package com.microsaas.contextlayer.dto;




public class ContextUpdateDTO {
    private String profile;
    private String preferences;
    private String attributes;

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
}
