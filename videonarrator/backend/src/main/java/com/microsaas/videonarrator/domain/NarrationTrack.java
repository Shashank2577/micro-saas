package com.microsaas.videonarrator.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "narration_tracks")
@Getter
@Setter
public class NarrationTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "voice_provider", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private VoiceProvider voiceProvider;

    @Column(name = "voice_id", nullable = false, length = 100)
    private String voiceId;

    @Column(name = "audio_url", columnDefinition = "TEXT")
    private String audioUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NarrationStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    public enum VoiceProvider {
        GOOGLE, OPENAI, ELEVENLABS
    }

    public enum NarrationStatus {
        PENDING, GENERATING, COMPLETED, FAILED
    }
}
