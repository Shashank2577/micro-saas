package com.tenantmanager.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customer_tenant")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTenant {
    @Id
    private UUID id;

    private UUID tenantId;

    private UUID externalTenantId;

    private String name;

    private String status;

    private Integer healthScore;

    private String churnRisk;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (externalTenantId == null) {
            externalTenantId = UUID.randomUUID();
        }
        if (healthScore == null) {
            healthScore = 100;
        }
        if (churnRisk == null) {
            churnRisk = "LOW";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
