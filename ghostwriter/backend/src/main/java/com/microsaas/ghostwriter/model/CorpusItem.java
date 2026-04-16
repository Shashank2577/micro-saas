package com.microsaas.ghostwriter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "corpus_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorpusItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "voice_model_id", nullable = false)
    private UUID voiceModelId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "word_count")
    private Integer wordCount;

    @Column(name = "added_at", updatable = false)
    private Instant addedAt;

    @PrePersist
    public void prePersist() {
        if (addedAt == null) {
            addedAt = Instant.now();
        }
        if (wordCount == null && content != null) {
            wordCount = content.split("\\s+").length;
        }
    }
}
