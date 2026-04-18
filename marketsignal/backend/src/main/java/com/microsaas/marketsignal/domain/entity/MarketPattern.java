package com.microsaas.marketsignal.domain.entity;

import com.microsaas.marketsignal.domain.enums.PatternType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "market_pattern")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketPattern {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "pattern_type", nullable = false)
    private PatternType patternType;

    @ManyToMany
    @JoinTable(
        name = "market_pattern_signals",
        joinColumns = @JoinColumn(name = "pattern_id"),
        inverseJoinColumns = @JoinColumn(name = "signal_id")
    )
    private List<MarketSignal> signals;

    @CreationTimestamp
    @Column(name = "detected_at", updatable = false)
    private OffsetDateTime detectedAt;
}
