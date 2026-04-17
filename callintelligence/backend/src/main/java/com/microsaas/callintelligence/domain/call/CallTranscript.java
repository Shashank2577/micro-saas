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
@Table(name = "call_transcripts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallTranscript {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "call_id", nullable = false)
    private Call call;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speaker_id")
    private CallSpeaker speaker;

    @Column(name = "start_time", nullable = false, precision = 10, scale = 2)
    private BigDecimal startTime;

    @Column(name = "end_time", nullable = false, precision = 10, scale = 2)
    private BigDecimal endTime;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "is_question")
    private Boolean isQuestion;

    @Column(name = "question_type")
    private String questionType; // OPEN, CLOSED

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;
}
