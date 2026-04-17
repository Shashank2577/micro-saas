package com.microsaas.observabilitystack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "health_checks")
public class HealthCheck {
    @Id
    private UUID id = UUID.randomUUID();
    private String tenantId;
    private String serviceName;
    private String endpoint;
    private String status;
    private LocalDateTime lastCheckedAt;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
