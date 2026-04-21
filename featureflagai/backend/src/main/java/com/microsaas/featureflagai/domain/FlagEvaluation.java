package com.microsaas.featureflagai.domain;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "flag_evaluations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlagEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "flag_id", nullable = false)
    private UUID flagId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private boolean result;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode context;

    @Column(name = "evaluated_at")
    private OffsetDateTime evaluatedAt;

    @PrePersist
    public void prePersist() {
        if (evaluatedAt == null) {
            evaluatedAt = OffsetDateTime.now();
        }
    }
}
