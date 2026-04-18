package com.microsaas.meetingbrain.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "transcript_lines")
public class TranscriptLine {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "meeting_id", nullable = false)
    private UUID meetingId;

    private String speaker;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(name = "start_timestamp")
    private Double startTimestamp;

    @Column(name = "end_timestamp")
    private Double endTimestamp;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    public TranscriptLine() {}

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public UUID getMeetingId() { return meetingId; }
    public void setMeetingId(UUID meetingId) { this.meetingId = meetingId; }
    public String getSpeaker() { return speaker; }
    public void setSpeaker(String speaker) { this.speaker = speaker; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public Double getStartTimestamp() { return startTimestamp; }
    public void setStartTimestamp(Double startTimestamp) { this.startTimestamp = startTimestamp; }
    public Double getEndTimestamp() { return endTimestamp; }
    public void setEndTimestamp(Double endTimestamp) { this.endTimestamp = endTimestamp; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
