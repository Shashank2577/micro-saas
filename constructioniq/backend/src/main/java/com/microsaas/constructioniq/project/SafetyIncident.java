package com.microsaas.constructioniq.project;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "safety_incidents", schema = "tenant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SafetyIncident {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "reported_by", nullable = false)
    private String reportedBy;

    @Column(name = "incident_date", nullable = false)
    private OffsetDateTime incidentDate;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String severity;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();
}
