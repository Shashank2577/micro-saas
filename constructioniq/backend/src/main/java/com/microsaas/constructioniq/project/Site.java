package com.microsaas.constructioniq.project;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "construction_sites", schema = "tenant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Site {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String name;

    private String address;

    @Column(name = "manager_name")
    private String managerName;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();
}
