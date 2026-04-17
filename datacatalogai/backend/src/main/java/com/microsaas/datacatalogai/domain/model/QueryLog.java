package com.microsaas.datacatalogai.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "query_logs")
public class QueryLog {
    @Id
    private UUID id = UUID.randomUUID();

    private String tenantId;

    private UUID assetId;

    private Instant executedAt;

    private String executorUserId;

    private Long durationMs;

    private Long rowsReturned;
}
