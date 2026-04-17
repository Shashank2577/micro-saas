package com.microsaas.datacatalogai.domain.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "glossary_terms")
public class GlossaryTerm {
    @Id
    private UUID id = UUID.randomUUID();

    private String tenantId;

    private String term;

    private String definition;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<UUID> linkedAssetIds;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
}
