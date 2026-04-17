package com.microsaas.callintelligence.domain.call;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "call_speakers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallSpeaker {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "call_id", nullable = false)
    private Call call;

    @Column(name = "speaker_label", nullable = false)
    private String speakerLabel;

    @Column(name = "speaker_name")
    private String speakerName;

    @Column(nullable = false)
    private String role; // REP, PROSPECT

    @Column(name = "talk_time_seconds")
    private Integer talkTimeSeconds;

    @Column(name = "talk_ratio", precision = 5, scale = 2)
    private BigDecimal talkRatio;

    @Column(name = "longest_monologue_seconds")
    private Integer longestMonologueSeconds;

    @Column(name = "sentiment_score", precision = 5, scale = 2)
    private BigDecimal sentimentScore;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;
}
