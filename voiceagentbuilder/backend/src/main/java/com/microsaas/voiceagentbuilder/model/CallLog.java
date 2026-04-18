package com.microsaas.voiceagentbuilder.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "call_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "agent_id", nullable = false)
    private UUID agentId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "caller_number")
    private String callerNumber;

    @Column(name = "duration_seconds")
    @Builder.Default
    private Integer durationSeconds = 0;

    @Builder.Default
    private String status = "IN_PROGRESS";

    @Builder.Default
    private BigDecimal cost = BigDecimal.ZERO;

    private String transcript;

    @Column(name = "recording_url")
    private String recordingUrl;

    @CreationTimestamp
    @Column(name = "started_at", updatable = false)
    private ZonedDateTime startedAt;

    @Column(name = "ended_at")
    private ZonedDateTime endedAt;
}
