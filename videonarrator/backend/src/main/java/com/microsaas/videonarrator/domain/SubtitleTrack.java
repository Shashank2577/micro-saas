package com.microsaas.videonarrator.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "subtitle_tracks")
@Getter
@Setter
public class SubtitleTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "transcription_id", nullable = false)
    private UUID transcriptionId;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "start_time_ms", nullable = false)
    private Long startTimeMs;

    @Column(name = "end_time_ms", nullable = false)
    private Long endTimeMs;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
