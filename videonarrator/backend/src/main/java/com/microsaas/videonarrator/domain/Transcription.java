package com.microsaas.videonarrator.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "transcriptions")
@Getter
@Setter
public class Transcription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "language_code", nullable = false, length = 10)
    private String languageCode;

    @Column(name = "full_text", columnDefinition = "TEXT")
    private String fullText;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TranscriptionStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    public enum TranscriptionStatus {
        PENDING, PROCESSING, COMPLETED, FAILED
    }
}
