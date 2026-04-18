package com.microsaas.insightengine.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "insight_comments")
@Data
public class InsightComment {
    @Id
    private UUID id = UUID.randomUUID();
    private UUID tenantId;
    private UUID insightId;
    private UUID userId;
    private String commentText;
    private LocalDateTime createdAt = LocalDateTime.now();
}
