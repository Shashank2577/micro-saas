package com.microsaas.observabilitystack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "incidents")
public class Incident {
    @Id
    private UUID id = UUID.randomUUID();
    private String tenantId;
    private String title;
    private String description;
    private String status;
    private String severity;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private LocalDateTime resolvedAt;
}
