package com.microsaas.integrationbridge.dto;

public class CreateIntegrationRequest {
    private String provider;
    private String authType;

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getAuthType() { return authType; }
    public void setAuthType(String authType) { this.authType = authType; }
}
