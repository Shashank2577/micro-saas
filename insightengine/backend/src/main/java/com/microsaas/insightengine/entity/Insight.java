package com.microsaas.insightengine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "insights")
@Data
public class Insight {
    @Id
    private UUID id = UUID.randomUUID();
    private UUID tenantId;
    private String type; // ANOMALY, CORRELATION, TREND, SEGMENT, COMPARISON
    private String title;
    private String description;
    private String explanation;
    private String recommendedAction;
    private Double impactScore;
    private Double confidenceScore;
    
    @Column(name = "metric_names", columnDefinition = "text[]")
    private List<String> metricNames;
    
    private String status = "NEW"; // NEW, ACKNOWLEDGED, RESOLVED
    private UUID assignedTo;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
