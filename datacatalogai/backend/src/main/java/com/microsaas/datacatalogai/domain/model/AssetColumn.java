package com.microsaas.datacatalogai.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "columns")
public class AssetColumn {
    @Id
    private UUID id = UUID.randomUUID();

    private String tenantId;

    private UUID assetId;

    private String name;

    private String dataType;

    private Boolean isNullable = true;

    private Boolean isPrimaryKey = false;

    private String piiCategory;

    private String description;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
}
