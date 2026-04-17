package com.microsaas.creatoranalytics.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "business_outcome")
public class BusinessOutcome {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutcomeType outcomeType;

    @Column(nullable = false)
    private UUID contentId;

    @Column(nullable = false)
    private UUID channelId;

    @Column(nullable = false)
    private BigDecimal attributedValue;

    @Column(nullable = false)
    private LocalDateTime occurredAt;
}
