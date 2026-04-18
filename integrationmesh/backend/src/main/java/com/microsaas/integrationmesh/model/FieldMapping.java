package com.microsaas.integrationmesh.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "field_mappings")
@Data
public class FieldMapping {
    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "integration_id")
    private Integration integration;

    @Column(name = "source_field")
    private String sourceField;

    @Column(name = "target_field")
    private String targetField;

    @Column(name = "transform_logic")
    private String transformLogic;

    @Column(name = "is_ai_suggested")
    private Boolean isAiSuggested = false;

    @Column(name = "confidence_score")
    private Double confidenceScore;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
