package com.microsaas.insightengine.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "alert_rules")
@Data
public class AlertRule {
    @Id
    private UUID id = UUID.randomUUID();
    private UUID tenantId;
    private String name;
    private String metricName;
    private String conditionType;
    private Double threshold;
    private String slackChannel;
    private Boolean isActive = true;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
