package com.microsaas.ghostwriter.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "voice_models")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoiceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String name;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "corpus_size")
    private Integer corpusSize;

    @Type(JsonType.class)
    @Column(name = "style_attributes", columnDefinition = "jsonb")
    private Map<String, Object> styleAttributes;

    @Column(name = "trained_at")
    private Instant trainedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModelStatus status;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (corpusSize == null) {
            corpusSize = 0;
        }
        if (status == null) {
            status = ModelStatus.READY;
        }
    }
}
