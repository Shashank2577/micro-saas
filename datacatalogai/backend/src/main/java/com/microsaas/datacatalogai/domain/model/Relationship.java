package com.microsaas.datacatalogai.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "relationships")
public class Relationship {
    @Id
    private UUID id = UUID.randomUUID();

    private String tenantId;

    private UUID fromAssetId;

    private UUID toAssetId;

    private String relType;

    private Double confidence;

    private Instant createdAt = Instant.now();
}
