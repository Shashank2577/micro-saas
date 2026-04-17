package com.microsaas.observabilitystack.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "alerts")
public class Alert {
    @Id
    private UUID id = UUID.randomUUID();
    private String tenantId;

    @ManyToOne
    @JoinColumn(name = "rule_id")
    private AlertRule rule;

    private String status;
    private LocalDateTime triggeredAt = LocalDateTime.now();
    private LocalDateTime resolvedAt;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
