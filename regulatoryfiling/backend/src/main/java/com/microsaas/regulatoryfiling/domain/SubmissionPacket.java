package com.microsaas.regulatoryfiling.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Type;
import io.hypersistence.utils.hibernate.type.json.JsonType;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "submission_packets")
public class SubmissionPacket {
    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false, length = 180)
    private String name;

    @Column(nullable = false, length = 40)
    private String status;

    @Type(JsonType.class)
    @Column(name = "metadata_json", columnDefinition = "jsonb")
    private String metadataJson;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
