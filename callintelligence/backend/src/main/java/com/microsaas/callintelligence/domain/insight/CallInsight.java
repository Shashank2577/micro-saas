package com.microsaas.callintelligence.domain.insight;

import com.microsaas.callintelligence.domain.call.Call;
import com.microsaas.callintelligence.domain.call.CallTranscript;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "call_insights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallInsight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "call_id", nullable = false)
    private Call call;

    @Column(name = "insight_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private InsightType insightType; // OBJECTION, COMPETITOR, COMMITMENT, ACTION_ITEM

    @Column(length = 100)
    private String category;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transcript_id")
    private CallTranscript transcript;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;
}
