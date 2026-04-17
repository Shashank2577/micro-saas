package com.microsaas.observabilitystack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "runbooks")
public class Runbook {
    @Id
    private UUID id = UUID.randomUUID();
    private String tenantId;
    private String name;
    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
