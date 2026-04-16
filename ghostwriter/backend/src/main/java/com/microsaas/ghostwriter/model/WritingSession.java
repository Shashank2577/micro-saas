package com.microsaas.ghostwriter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "writing_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WritingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "voice_model_id")
    private UUID voiceModelId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String topic;

    @Column(name = "target_word_count")
    private Integer targetWordCount;

    @Column(name = "current_draft", columnDefinition = "TEXT")
    private String currentDraft;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (targetWordCount == null) {
            targetWordCount = 0;
        }
        if (status == null) {
            status = SessionStatus.OUTLINE;
        }
    }
}
