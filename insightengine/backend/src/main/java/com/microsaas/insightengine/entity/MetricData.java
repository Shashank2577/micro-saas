package com.microsaas.insightengine.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "metric_data")
@Data
public class MetricData {
    @Id
    private UUID id = UUID.randomUUID();
    private UUID tenantId;
    private String metricName;
    private Double metricValue;
    private LocalDateTime timestamp;
    private String segment;
    private LocalDateTime createdAt = LocalDateTime.now();
}
