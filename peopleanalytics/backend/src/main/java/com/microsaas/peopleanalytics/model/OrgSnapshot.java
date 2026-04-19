package com.microsaas.peopleanalytics.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "org_snapshots")
public class OrgSnapshot {
    @Id
    private UUID id;
    private UUID tenantId;
    private String name;
    private String status;

    @JdbcTypeCode(SqlTypes.JSON)
    private String metadataJson;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
