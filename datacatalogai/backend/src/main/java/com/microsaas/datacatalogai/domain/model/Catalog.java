package com.microsaas.datacatalogai.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "catalogs")
public class Catalog {
    @Id
    private UUID id = UUID.randomUUID();

    private String tenantId;

    private String name;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
}
