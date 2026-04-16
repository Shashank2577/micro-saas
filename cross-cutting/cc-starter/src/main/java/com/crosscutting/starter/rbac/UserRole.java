package com.crosscutting.starter.rbac;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_roles", schema = "cc")
@IdClass(UserRoleId.class)
public class UserRole {
    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Id
    @Column(name = "role_id", nullable = false)
    private UUID roleId;

    @Id
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "assigned_at", nullable = false, updatable = false)
    private Instant assignedAt;

    @PrePersist
    protected void onCreate() {
        this.assignedAt = Instant.now();
    }
}
