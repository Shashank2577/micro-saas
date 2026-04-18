package com.microsaas.integrationbridge.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_credentials")
public class ApiCredential {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "integration_id", nullable = false)
    private UUID integrationId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "encrypted_token", length = 2048)
    private String encryptedToken;

    @Column(name = "refresh_token", length = 2048)
    private String refreshToken;

    @Column(name = "token_expiry")
    private LocalDateTime tokenExpiry;

    private String username;

    @Column(name = "encrypted_password", length = 2048)
    private String encryptedPassword;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getIntegrationId() { return integrationId; }
    public void setIntegrationId(UUID integrationId) { this.integrationId = integrationId; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getEncryptedToken() { return encryptedToken; }
    public void setEncryptedToken(String encryptedToken) { this.encryptedToken = encryptedToken; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public LocalDateTime getTokenExpiry() { return tokenExpiry; }
    public void setTokenExpiry(LocalDateTime tokenExpiry) { this.tokenExpiry = tokenExpiry; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEncryptedPassword() { return encryptedPassword; }
    public void setEncryptedPassword(String encryptedPassword) { this.encryptedPassword = encryptedPassword; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
