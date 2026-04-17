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
@Table(name = "assets")
public class Asset {
    @Id
    private UUID id = UUID.randomUUID();

    private String tenantId;

    private UUID sourceId;

    private String fqn;

    private String type;

    private Long rowCountEstimate;

    private String ownerId;

    private Integer trustScore = 0;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> tagsJson;

    private String descriptionAi;
    private String descriptionHuman;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
}
