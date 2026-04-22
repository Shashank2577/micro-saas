package com.microsaas.callintelligence.domain.insight;

import com.microsaas.callintelligence.domain.call.Call;
import com.microsaas.callintelligence.domain.call.CallSpeaker;
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
@Table(name = "sentiment_analysis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SentimentAnalysis {
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

    @Column(name = "overall_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal overallScore;

    @Column(name = "positivity_ratio", precision = 5, scale = 2)
    private BigDecimal positivityRatio;

    @Column(name = "negativity_ratio", precision = 5, scale = 2)
    private BigDecimal negativityRatio;

    @CreationTimestamp
    @Column(name = "analyzed_at", updatable = false)
    private OffsetDateTime analyzedAt;
}
