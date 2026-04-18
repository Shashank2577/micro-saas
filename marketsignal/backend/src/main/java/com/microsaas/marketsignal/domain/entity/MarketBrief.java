package com.microsaas.marketsignal.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "market_brief")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketBrief {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String summary;

    @Column(columnDefinition = "text")
    private String content;

    @CreationTimestamp
    @Column(name = "generated_at", updatable = false)
    private OffsetDateTime generatedAt;
}
