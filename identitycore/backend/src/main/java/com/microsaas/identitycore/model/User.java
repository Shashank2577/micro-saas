package com.microsaas.identitycore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    private UUID id;
    private UUID tenantId;
    private String email;
    private String fullName;
    private String department;
    private String role;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public User() {}

    public User(UUID id, UUID tenantId, String email, String fullName, String department, String role, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.email = email;
        this.fullName = fullName;
        this.department = department;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
