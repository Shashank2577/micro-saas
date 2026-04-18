package com.microsaas.workspacemanager.dto;


import java.util.UUID;

public class WorkspaceDto {
    private String name;
    private String subdomain;
    private String brandingLogoUrl;
    private String features;

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public String getSubdomain() { return this.subdomain; }
    public void setSubdomain(String subdomain) { this.subdomain = subdomain; }

    public String getBrandingLogoUrl() { return this.brandingLogoUrl; }
    public void setBrandingLogoUrl(String brandingLogoUrl) { this.brandingLogoUrl = brandingLogoUrl; }

    public String getFeatures() { return this.features; }
    public void setFeatures(String features) { this.features = features; }
}
