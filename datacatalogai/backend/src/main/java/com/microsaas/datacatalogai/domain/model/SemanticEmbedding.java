package com.microsaas.datacatalogai.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "semantic_embeddings")
public class SemanticEmbedding {
    @Id
    private UUID id = UUID.randomUUID();

    private String tenantId;

    private UUID assetId;

    @Column(columnDefinition = "vector")
    private String vector;

    private String textSource;

    private Instant createdAt = Instant.now();
}
